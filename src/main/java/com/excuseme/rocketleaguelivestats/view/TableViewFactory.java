package com.excuseme.rocketleaguelivestats.view;

import com.excuseme.rocketleaguelivestats.model.GamingSystem;
import com.excuseme.rocketleaguelivestats.model.Rank;
import com.excuseme.rocketleaguelivestats.model.Skill;
import com.excuseme.rocketleaguelivestats.view.model.PlayerViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;

public class TableViewFactory {
    public static final double RANK_COLUMN_WIDTH = 160d;
    public static final String STYLE_OWN_PLAYER = "styleOwnPlayer";

    private static final Comparator<Rank> RANK_COMPARATOR = new Comparator<Rank>() {
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

    public static TableView create() {
        final TableView table = new TableView();
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        table.setMaxWidth(915);
//        table.setMaxHeight(400);
//        table.setPrefWidth(915);
//        table.setPrefHeight(400);

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
                                stringBuilder.append(String.valueOf(rank.getTier().getText()));
                                if (rank.getDivision() != null) {
                                    stringBuilder.append("\nDivision: ").append(rank.getDivision());
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
        systemCol.setMaxWidth(60);
        systemCol.setMinWidth(60);

        final Callback<TableColumn<PlayerViewModel, GamingSystem>, TableCell<PlayerViewModel, GamingSystem>> gamingSystemColumnRenderer = new Callback<TableColumn<PlayerViewModel, GamingSystem>, TableCell<PlayerViewModel, GamingSystem>>() {
            public TableCell<PlayerViewModel, GamingSystem> call(TableColumn<PlayerViewModel, GamingSystem> tableColumn) {
                final TableCell<PlayerViewModel, GamingSystem> tableCell = new TableCell<PlayerViewModel, GamingSystem>() {

                    protected void updateItem(GamingSystem gamingSystem, boolean empty) {
                        super.updateItem(gamingSystem, empty);
                        getStyleClass().add("-fx-graphic-text-gap: 1em;");
                        if (gamingSystem != null && gamingSystem.getIconPath() != null) {
                            final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(gamingSystem.getIconPath());
                            final Image image = new Image(resourceAsStream, 30d, 30d, true, true);

                            setGraphic(new ImageView(image));
                            setText("");
                        } else {
                            setGraphic(null);
                            if(gamingSystem != null) {
                                setText(gamingSystem.name());
                            } else {
                                setText("");
                            }
                        }
                        setAlignment(Pos.CENTER);

                    }
                };
                return tableCell;
            }
        };
        systemCol.setCellFactory(gamingSystemColumnRenderer);

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<PlayerViewModel, String>("name"));
        nameCol.setCellFactory(styleNotActive);
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
        return table;
    }
}
