package com.excuseme.rocketleaguelivestats.view;

import com.excuseme.rocketleaguelivestats.model.Game;
import com.excuseme.rocketleaguelivestats.model.GamingSystem;
import com.excuseme.rocketleaguelivestats.model.PlayerIdentifier;
import com.excuseme.rocketleaguelivestats.model.Statistics;
import com.excuseme.rocketleaguelivestats.repository.CachedStatisticsRepository;
import com.excuseme.rocketleaguelivestats.repository.GameRepository;
import com.excuseme.rocketleaguelivestats.repository.NoLogException;
import com.excuseme.rocketleaguelivestats.repository.StatisticsRepository;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.log4j.Logger;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class MainTableView extends Application {
    private static final Logger LOG = Logger.getLogger(MainTableView.class);
    public static final String ROW_TABLE_CSS = "row-table.css";
    public static final String STYLE_OWN_PLAYER = "styleOwnPlayer";
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
        stage.setWidth(730);
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
        final Callback<TableColumn<PlayerViewModel, Integer>, TableCell<PlayerViewModel, Integer>> styleNotActiveInteger = new Callback<TableColumn<PlayerViewModel, Integer>, TableCell<PlayerViewModel, Integer>>() {
            public TableCell<PlayerViewModel, Integer> call(TableColumn<PlayerViewModel, Integer> tableColumn) {
                return new TableCell<PlayerViewModel, Integer>() {

                    protected void updateItem(Integer example, boolean empty) {
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
                        setText(example != null ? String.valueOf(example) : "");
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
        nameCol.setMinWidth(300);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("name"));
        nameCol.setCellFactory(styleNotActive);
        table.setPrefHeight(250);
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
        oneVsOneCol.setCellFactory(styleNotActiveInteger);

        TableColumn twoVSTwoCol = new TableColumn("Doubles");
        twoVSTwoCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("twoVsTwo"));
        twoVSTwoCol.setCellFactory(styleNotActiveInteger);

        TableColumn threeVSThreeSoloCol = new TableColumn("Solo Standard");
        threeVSThreeSoloCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("threeVsThreeSolo"));
        threeVSThreeSoloCol.setCellFactory(styleNotActiveInteger);

        TableColumn threeVSThreeStandardCol = new TableColumn("Standard");
        threeVSThreeStandardCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("threeVsThreeStandard"));
        threeVSThreeStandardCol.setCellFactory(styleNotActiveInteger);

        table.getColumns().addAll(systemCol, nameCol, oneVsOneCol, twoVSTwoCol, threeVSThreeSoloCol, threeVSThreeStandardCol);
//        averageCol.setSortType(TableColumn.SortType.DESCENDING);

        final Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                executor.submit(reload);
            }
        });
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

        executor.scheduleAtFixedRate(reload, 0, 20, TimeUnit.SECONDS);

    }

    private void reloadData() {
        LOG.info("Reloading data....");
        GameRepository gameRepository = GameRepository.createDefault();
        Game latestGame = null;
        try {
            latestGame = gameRepository.findLatestGame();
        } catch (final NoLogException e) {
            Platform.runLater(new Runnable() {
                public void run() {
                    final Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No Log file found");
                    alert.setHeaderText("No Log file found. Exiting the application...");
                    alert.setContentText(e.getMessage());

                    alert.showAndWait();
                    Platform.exit();
                }
            });
        }
        if (latestGame != null) {
            if (!gameViewModel.isSameGame(latestGame)) {
                LOG.info("Something changed, updating");
                gameViewModel.updateGame(latestGame);
                gameUpdated();
                final ObservableList<PlayerViewModel> playerViewModels = PlayerViewModelMapper.map(latestGame.getPlayers());
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
                                        playerViewModel.setAverage(statistics.getAverage());
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
//            gameViewModel.updateGame(null);
//            updateItems(FXCollections.<PlayerViewModel>emptyObservableList());
//            gameUpdated();
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

    Runnable reload = new Runnable() {
        public void run() {
            try {
                reloadData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

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

}