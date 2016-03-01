package com.excuseme.rocketleaguelivestats.view;

import com.excuseme.rocketleaguelivestats.model.*;
import com.excuseme.rocketleaguelivestats.repository.CachedStatisticsRepository;
import com.excuseme.rocketleaguelivestats.repository.GameDataListener;
import com.excuseme.rocketleaguelivestats.repository.GameMapper;
import com.excuseme.rocketleaguelivestats.scanner.TailingFileScanner;
import com.excuseme.rocketleaguelivestats.scanner.model.GameData;
import com.excuseme.rocketleaguelivestats.scanner.model.SessionData;
import com.excuseme.rocketleaguelivestats.view.model.GameViewModel;
import com.excuseme.rocketleaguelivestats.view.model.PlayerViewModel;
import com.excuseme.rocketleaguelivestats.view.model.mapper.PlayerViewModelMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
            return o1.compareTo(o2);
        }
    };
    public static final double RANK_COLUMN_WIDTH = 160d;
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
        table.setEditable(true);
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(oldValue != null) ((PlayerViewModel) oldValue).setExpand(false);
                if(newValue != null) ((PlayerViewModel) newValue).setExpand(true);
                ((TableColumn) table.getColumns().get(0)).setVisible(false);
                ((TableColumn) table.getColumns().get(0)).setVisible(true);
            }
        });
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
                        final StringBuilder stringBuilder = new StringBuilder();

                        final TableRow tableRow = getTableRow();
                        if (tableRow != null && tableRow.getItem() != null) {
                            final PlayerViewModel playerViewModel = (PlayerViewModel) tableRow.getItem();
                            if (!playerViewModel.getActive()) {
                                getStyleClass().add("styleNotActive");

                            }
                            if(rank != null) {
                                if(rank.getLeaderboardPosition() != null) {
                                    stringBuilder.append("#" ).append(rank.getLeaderboardPosition()).append(" ");
                                }
                                stringBuilder.append(String.valueOf(rank.getTier().getText()));
                                if (rank.getDivision() != null) {
                                    stringBuilder.append("\nDivision: ").append(rank.getDivision());
                                }
                                if (rank.getRating() != null && rank.getRating() >0) {
                                    stringBuilder.append("\nRating: ").append(rank.getRating());
                                }
                                final Skill skill = rank.getSkill();
                                if(skill != null) {
                                    if (skill.getMmr() != null) {
                                        stringBuilder.append("\nMMR: ").append(skill.getMmr().toString());
                                    }
                                    if(playerViewModel.getExpand()) {
                                        if (skill.getMu() != null) {
                                            stringBuilder.append("\nMu: ").append(skill.getMu().toString());
                                        }
                                        if (skill.getSigma() != null) {
                                            stringBuilder.append("\nSigma: ").append(skill.getSigma().toString());
                                        }
                                        if (skill.getMatchesPlayed() != null) {
                                            stringBuilder.append("\nMatches played: ").append(skill.getMatchesPlayed().toString());
                                        }
                                    }
                                }
                            }
                        }

                        setText(stringBuilder.toString());
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
        table.setPrefHeight(600);
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
//                        setPrefHeight(40d);
                    }
                };
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2 && (!row.isEmpty())) {
                            PlayerViewModel rowData = row.getItem();
                            final PlayerIdentifier playerIdentifier = rowData.getPlayerIdentifier();
                            if (!GamingSystem.BOT.equals(playerIdentifier.getGamingSystem())) {
                                try {
                                    final String url = statisticsRepository.createUrl(playerIdentifier);
                                    if(url != null) {
                                        openWebpage(new URI(url));
                                    }
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
                final List<PlayerIdentifier> playerIdentifiers = playerViewModels.stream().map(p -> p.getPlayerIdentifier()).collect(Collectors.toList());
                try {
                    final Map<PlayerIdentifier, Statistics> map = statisticsRepository.findAll(game.getIdentifier(), playerIdentifiers);
                    map.keySet().stream().forEach(k->playerViewModels.stream().filter(m->Objects.equals(k, m.getPlayerIdentifier())).findFirst().ifPresent(m-> {
                        final Statistics statistics = map.get(k);
                        if(statistics != null) {
                            m.setOneVsOne(statistics.getOneVsOne());
                            m.setTwoVsTwo(statistics.getTwoVsTwo());
                            m.setThreeVsThreeStandard(statistics.getThreeVsThree());
                            m.setThreeVsThreeSolo(statistics.getThreeVSThreeSolo());
                        }
                    }));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateItems(playerViewModels);
            } else {
                LOG.info("Same game, won't update");
            }
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
    public void sessionDataChanged(SessionData sessionData) {
        statisticsRepository.updateSessionData(sessionData);
    }

    @Override
    public void gameDataChanged(GameData gameData) {
        final Game game = GameMapper.map(gameData);
        reloadData(game);
    }
}