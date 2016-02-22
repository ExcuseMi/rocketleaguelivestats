package com.excuseme.rocketleaguelivestats.view;

import com.excuseme.rocketleaguelivestats.model.*;
import com.excuseme.rocketleaguelivestats.repository.*;
import com.excuseme.rocketleaguelivestats.scanner.TailingFileScanner;
import com.excuseme.rocketleaguelivestats.scanner.model.GameData;
import com.excuseme.rocketleaguelivestats.view.model.GameViewModel;
import com.excuseme.rocketleaguelivestats.view.model.PlayerViewModel;
import com.excuseme.rocketleaguelivestats.view.model.mapper.PlayerViewModelMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.log4j.Logger;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class MainTableView extends Application implements GameDataListener{
    private static final String FOLDER = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + File.separator + "my games" + File.separator + "Rocket League" + File.separator + "TAGame" + File.separator + "Logs" + File.separator;
    private static final String LAUNCH = "Launch.log";

    private static final Logger LOG = Logger.getLogger(MainTableView.class);
    public static final String ROW_TABLE_CSS = "row-table.css";
    public static final String STYLE_OWN_PLAYER = "styleOwnPlayer";
    public static final Comparator<Rank> RANK_COMPARATOR = new Comparator<Rank>() {
        @Override
        public int compare(Rank o1, Rank o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return +1;
            }
            if (!Objects.equals(o1.getTier().getTier(), o2.getTier().getTier())) {
                return Integer.compare(o1.getTier().getTier(), o2.getTier().getTier());
            } else {
                return Integer.compare(o1.getRating() != null ? o1.getRating() : -1, o2.getRating() != null ? o2.getRating() : -1);
            }
        }
    };
    public static final double RANK_COLUMN_WIDTH = 140d;
    private final GameViewModel gameViewModel;
    private javafx.scene.control.TableView table = new javafx.scene.control.TableView();
    private ScheduledExecutorService executor;
    private CachedStatisticsRepository statisticsRepository;
    private final Timer timer = new Timer("scraper", false);

    public MainTableView() {
        executor = Executors.newScheduledThreadPool(1);
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
//        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.setAlwaysOnTop(true);
//        stage.initStyle(StageStyle.UNIFIED);
        stage.initStyle(StageStyle.UNIFIED);
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("rlicon.png")));

        stage.setTitle("Rocket league Live Stats");
        stage.setWidth(870);
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
        table.setEditable(true);

        final Callback<TableColumn<PlayerViewModel, String>, TableCell<PlayerViewModel, String>> styleNotActive = new Callback<TableColumn<PlayerViewModel, String>, TableCell<PlayerViewModel, String>>() {
            public TableCell<PlayerViewModel, String> call(TableColumn<PlayerViewModel, String> tableColumn) {
                return new TableCell<PlayerViewModel, String>() {

                    protected void updateItem(String example, boolean empty) {
                        super.updateItem(example, empty);
                        // Reset column styles
                        if (getStyleClass() != null) {
                            String[] possibleStyles = new String[]{"styleNotActive"};

                            for (String style : possibleStyles) {
                                if (getStyleClass().contains(style)) {
                                    // Will not reset background, even though style is removed?
                                    getStyleClass().remove(style);
                                }
                            }
                        }
                        final TableRow tableRow = getTableRow();
                        if (tableRow != null && tableRow.getItem() != null) {
                            final PlayerViewModel playerViewModel = (PlayerViewModel) tableRow.getItem();
                            if (!playerViewModel.getActive()) {
                                getStyleClass().add("styleNotActive");

                            }
                        }
                        setText(example);
                    }
                };
            }
        };
        final Callback<TableColumn<PlayerViewModel, Rank>, TableCell<PlayerViewModel, Rank>> styleNotActiveInteger = new Callback<TableColumn<PlayerViewModel, Rank>, TableCell<PlayerViewModel, Rank>>() {
            public TableCell<PlayerViewModel, Rank> call(TableColumn<PlayerViewModel, Rank> tableColumn) {
                return new TableCell<PlayerViewModel, Rank>() {

                    protected void updateItem(Rank rank, boolean empty) {
                        super.updateItem(rank, empty);
                        // Reset column styles
                        if (getStyleClass() != null) {
                            String[] possibleStyles = new String[]{"styleNotActive"};

                            for (String style : possibleStyles) {
                                if (getStyleClass().contains(style)) {
                                    // Will not reset background, even though style is removed?
                                    getStyleClass().remove(style);
                                }
                            }
                        }
                        final TableRow tableRow = getTableRow();
                        if (tableRow != null && tableRow.getItem() != null) {
                            final PlayerViewModel playerViewModel = (PlayerViewModel) tableRow.getItem();
                            if (!playerViewModel.getActive()) {
                                getStyleClass().add("styleNotActive");

                            }
                        }
                        String value = rank != null ? String.valueOf(rank.getTier().getText()) : "";
                        value += rank != null && rank.getRating() != null ? "\n(Rating " + rank.getRating() + ")" : "";
                        setText(value);
                        if(rank != null && rank.getTier() != null) {
                            final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("ranks/" + rank.getTier().getTier() + ".png");
                            final Image image = new Image(resourceAsStream,30d,30d, true, true);

                            setGraphic(new ImageView(image));
                        } else {
                            setGraphic(null);
                        }

                    }
                };
            }
        };

        TableColumn systemCol = new TableColumn("System");
        systemCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("system"));
        systemCol.setCellFactory(styleNotActive);

//        systemCol.setCellFactory(callback);
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("name"));
        nameCol.setCellFactory(styleNotActive);
        table.setPrefHeight(400);
        table.setRowFactory(new Callback<TableView<PlayerViewModel>, TableRow<PlayerViewModel>>() {
            public TableRow<PlayerViewModel> call(TableView<PlayerViewModel> tableView) {
                final TableRow<PlayerViewModel> row = new TableRow<PlayerViewModel>() {
                    @Override
                    protected void updateItem(PlayerViewModel playerViewModel, boolean empty) {
                        super.updateItem(playerViewModel, empty);
                        final PlayerViewModel item = getItem();
                        if (item != null && item.isOwnPlayer()) {
                            if (!getStyleClass().contains("styleOwnPlayer")) {
                                getStyleClass().add(STYLE_OWN_PLAYER);
                            }
                        } else {
                            getStyleClass().removeAll(Collections.singleton("styleOwnPlayer"));
                        }
                        setPrefHeight(40d);
                    }
                };
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2 && (!row.isEmpty())) {
                            PlayerViewModel rowData = row.getItem();
                            final PlayerIdentifier playerIdentifier = rowData.getPlayerIdentifier();
                            if (!GamingSystem.BOT.equals(playerIdentifier.getGamingSystem())) {
                                try {
                                    openWebpage(new URI(StatisticsRepository.createURL(playerIdentifier.getPlayerId(), playerIdentifier.getGamingSystem().getQualifier())));
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                return row;
            }
        });

        TableColumn oneVsOneCol = new TableColumn("Duel");
        oneVsOneCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("oneVsOne"));
        oneVsOneCol.setComparator(RANK_COMPARATOR);
        oneVsOneCol.setCellFactory(styleNotActiveInteger);
        oneVsOneCol.setMinWidth(RANK_COLUMN_WIDTH);
        TableColumn twoVSTwoCol = new TableColumn("Doubles");
        twoVSTwoCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("twoVsTwo"));
        twoVSTwoCol.setCellFactory(styleNotActiveInteger);
        twoVSTwoCol.setComparator(RANK_COMPARATOR);
        twoVSTwoCol.setMinWidth(RANK_COLUMN_WIDTH);

        TableColumn threeVSThreeSoloCol = new TableColumn("Solo Standard");
        threeVSThreeSoloCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("threeVsThreeSolo"));
        threeVSThreeSoloCol.setCellFactory(styleNotActiveInteger);
        threeVSThreeSoloCol.setComparator(RANK_COMPARATOR);
        threeVSThreeSoloCol.setMinWidth(RANK_COLUMN_WIDTH);

        TableColumn threeVSThreeStandardCol = new TableColumn("Standard");
        threeVSThreeStandardCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("threeVsThreeStandard"));
        threeVSThreeStandardCol.setCellFactory(styleNotActiveInteger);
        threeVSThreeStandardCol.setComparator(RANK_COMPARATOR);
        threeVSThreeStandardCol.setMinWidth(RANK_COLUMN_WIDTH);

        table.getColumns().addAll(systemCol, nameCol, oneVsOneCol, twoVSTwoCol, threeVSThreeSoloCol, threeVSThreeStandardCol);

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
                    if(table.getItems() != null) {
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
        LOG.info("Reloading data....");
        if (game != null) {
            if (!gameViewModel.isSameGame(game)) {
                LOG.info("Something changed, updating");
                gameViewModel.updateGame(game);
                gameUpdated();
                final ObservableList<PlayerViewModel> playerViewModels = PlayerViewModelMapper.map(game.getPlayers());
                int i = 0;
                for (final PlayerViewModel playerViewModel : playerViewModels) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                PlayerIdentifier playerIdentifier = playerViewModel.getPlayerIdentifier();
                                if (!GamingSystem.BOT.equals(playerIdentifier.getGamingSystem())) {
                                    Statistics statistics = statisticsRepository.find(gameViewModel.getGameIdentifier(), playerIdentifier);
                                    if (statistics != null) {
                                        playerViewModel.setOneVsOne(statistics.getOneVsOne());
                                        playerViewModel.setTwoVsTwo(statistics.getTwoVsTwo());
                                        playerViewModel.setThreeVsThreeSolo(statistics.getThreeVSThreeSolo());
                                        playerViewModel.setThreeVsThreeStandard(statistics.getThreeVsThree());
                                        Platform.runLater(new Runnable() {
                                                              public void run() {
                                                                  table.sort();

                                                              }
                                                          }
                                        );

                                    } else {
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, i);
                    i+=2000;
                }

                updateItems(playerViewModels);
            } else {
                LOG.info("Same game, won't update");
            }
        } else {

        }
    }

    private void updateItems(final ObservableList<PlayerViewModel> playerViewModels) {
        Platform.runLater(new Runnable() {
            public void run() {
                final FilteredList<PlayerViewModel> filteredList = new FilteredList<PlayerViewModel>(playerViewModels);
                filteredList.setPredicate(new Predicate<PlayerViewModel>() {
                    public boolean test(PlayerViewModel playerViewModel) {
                        return !GamingSystem.BOT.equals(playerViewModel.getPlayerIdentifier().getGamingSystem());
                    }
                });
                final SortedList<PlayerViewModel> sortedData = new SortedList<PlayerViewModel>(filteredList);
                sortedData.comparatorProperty().bind(table.comparatorProperty());
                table.setItems(sortedData);
                table.sort();

            }
        });
    }

    private void gameUpdated() {
        Platform.runLater(new Runnable() {
            public void run() {
                table.getSortOrder().clear();
                for (Object o : table.getColumns()) {
                    final TableColumn tableColumn = (TableColumn) o;
                    tableColumn.getStyleClass().clear();
                }
                if(gameViewModel != null) {
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


    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void gameDataChanged(GameData gameData) {
        final Game game = GameMapper.map(gameData);
        reloadData(game);
    }
}