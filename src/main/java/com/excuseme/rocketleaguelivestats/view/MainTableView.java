package com.excuseme.rocketleaguelivestats.view;

import com.excuseme.rocketleaguelivestats.model.Game;
import com.excuseme.rocketleaguelivestats.model.GamingSystem;
import com.excuseme.rocketleaguelivestats.model.PlayerIdentifier;
import com.excuseme.rocketleaguelivestats.model.Statistics;
import com.excuseme.rocketleaguelivestats.repository.CachedStatisticsRepository;
import com.excuseme.rocketleaguelivestats.repository.GameDataListener;
import com.excuseme.rocketleaguelivestats.repository.GameMapper;
import com.excuseme.rocketleaguelivestats.scanner.TailingFileScanner;
import com.excuseme.rocketleaguelivestats.scanner.model.GameData;
import com.excuseme.rocketleaguelivestats.view.model.GameViewModel;
import com.excuseme.rocketleaguelivestats.view.model.PlayerViewModel;
import com.excuseme.rocketleaguelivestats.view.model.mapper.PlayerViewModelMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainTableView extends Application implements GameDataListener {
    private static final String FOLDER = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + File.separator + "my games" + File.separator + "Rocket League" + File.separator + "TAGame" + File.separator + "Logs" + File.separator;
    private static final String LAUNCH = "Launch.log";

    public static final String ROW_TABLE_CSS = "row-table.css";

    private final GameViewModel gameViewModel;
    private javafx.scene.control.TableView table;
    private CachedStatisticsRepository statisticsRepository;
    private ObservableList<PlayerViewModel> playerViewModels= FXCollections.observableArrayList();


    public MainTableView() {
        this.statisticsRepository = new CachedStatisticsRepository();
        gameViewModel = new GameViewModel();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        final Group root = new Group();
        root.setId("pane");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource(ROW_TABLE_CSS).toExternalForm());
        stage.initStyle(StageStyle.UNIFIED);
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("rlicon.png")));

        stage.setTitle("Rocket league Live Stats");
        stage.setWidth(950);
        stage.setResizable(false);

        stage.setOnCloseRequest(new EventHandler() {
            public void handle(Event event) {
                Platform.exit();
                System.exit(0);
            }
        });
        final Label label = new Label("Most recent game");
        label.setFont(new Font("Arial", 20));
        final Label gameLabel = new Label("");
        gameLabel.setFont(new Font("Arial", 20));

        gameLabel.textProperty().bind(gameViewModel.labelPropertyProperty());
        table = TableViewFactory.create();
        table.setItems(createList());
        final VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0, 10, 0, 10));
        vbox.getChildren().addAll(gameLabel, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
        final KeyCombination ctrlC = KeyCodeCombination.keyCombination("Ctrl+C");
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent ke) {
                if (ctrlC.match(ke)) {
                    if (table.getItems() != null) {
                        Clipboard clipboard = Clipboard.getSystemClipboard();
                        final ClipboardContent content = new ClipboardContent();
                        final ObservableList<PlayerViewModel> items = table.getItems();
                        final StringBuilder stringBuilder = new StringBuilder();
                        for (PlayerViewModel playerViewModel : items) {
                            stringBuilder.append(playerViewModel.toString()).append("\n");
                        }
                        final String text = stringBuilder.toString();
                        content.putString(text);
                        clipboard.setContent(content);
                    }
                }
            }
        });
        final TailingFileScanner tailingFileScanner = new TailingFileScanner(this, new File(FOLDER + LAUNCH));
    }

    private void reloadData(Game game) {
        if (game != null) {
            if (!gameViewModel.isSameGame(game)) {
                gameViewModel.updateGame(game);
                gameUpdated();
                playerViewModels.clear();
                addNewPlayers(game);
                updateItems();
            } else if(gameViewModel.isGameUpdated(game)) {
                gameViewModel.updateGame(game);
                addNewPlayers(game);
                updateItems();
            } else {
            }
        }
    }

    private void addNewPlayers(final Game game) {
        PlayerViewModelMapper.map(game.getPlayers(), playerViewModels);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                final List<PlayerIdentifier> playerIdentifiers = playerViewModels.stream().filter(p->!p.hasStatistics() && !GamingSystem.BOT.equals(p.getPlayerIdentifier().getGamingSystem())).map(PlayerViewModel::getPlayerIdentifier).collect(Collectors.toList());
                    final Map<PlayerIdentifier, Statistics> map = statisticsRepository.findAll(game.getIdentifier(), playerIdentifiers);
                    map.keySet().stream().forEach(k -> playerViewModels.stream().filter(m -> Objects.equals(k, m.getPlayerIdentifier())).findFirst().ifPresent(m -> {
                        final Statistics statistics = map.get(k);
                        if (statistics != null) {
                                m.setOneVsOne(statistics.getOneVsOne());
                                m.setTwoVsTwo(statistics.getTwoVsTwo());
                                m.setThreeVsThreeStandard(statistics.getThreeVsThree());
                                m.setThreeVsThreeSolo(statistics.getThreeVSThreeSolo());
                                m.setHasStatistics(true);
                            updateItems();
                        }
                    }));


                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void updateTableSizeAutomatically() {
        Platform.runLater(() -> {
            ((TableColumn) table.getColumns().get(0)).setVisible(false);
            ((TableColumn) table.getColumns().get(0)).setVisible(true);
        });
    }

    private void updateItems() {
        Platform.runLater(new Runnable() {
            public void run() {
                table.sort();
                updateTableSizeAutomatically();
            }
        });
    }

    private SortedList<PlayerViewModel> createList() {
        final FilteredList<PlayerViewModel> filteredList = new FilteredList<PlayerViewModel>(playerViewModels);
        filteredList.setPredicate(new Predicate<PlayerViewModel>() {
            public boolean test(PlayerViewModel playerViewModel) {
                return !GamingSystem.BOT.equals(playerViewModel.getPlayerIdentifier().getGamingSystem());
            }
        });
        final SortedList<PlayerViewModel> sortedData = new SortedList<PlayerViewModel>(filteredList);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        return sortedData;
    }

    private void gameUpdated() {
        Platform.runLater(new Runnable() {
            public void run() {
                table.getSortOrder().clear();
                for (Object o : table.getColumns()) {
                    final TableColumn tableColumn = (TableColumn) o;
                    tableColumn.getStyleClass().clear();
                }
                if (gameViewModel != null) {
                    final Integer mostImportantColumn = gameViewModel.calculateMostImportantColumn();
                    if (mostImportantColumn != null) {
                        final TableColumn column = (TableColumn) table.getColumns().get(mostImportantColumn);
                        table.getSortOrder().add(column);
                        column.setSortType(TableColumn.SortType.DESCENDING);
                        column.getStyleClass().add("styleImportant");
                        table.sort();
                    }
                }
            }
        });
    }



    @Override
    public void gameDataChanged(GameData gameData) {
        final Game game = GameMapper.map(gameData);
        reloadData(game);
    }
}