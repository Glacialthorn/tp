package seedu.estatemate.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.estatemate.commons.core.LogsCenter;
import seedu.estatemate.model.Model;
import seedu.estatemate.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class TenantListPanel extends UiPart<Region> {
    private static final String FXML = "TenantListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TenantListPanel.class);
    private final Model model;

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public TenantListPanel(ObservableList<Person> personList, Model model) {
        super(FXML);
        this.model = model;
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
    }

    /**
     * Automatically refresh the displayed item after modification.
     */
    public void refresh() {
        personListView.refresh();
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code TenantCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new TenantCard(person, getIndex() + 1, model).getRoot());
            }
        }
    }

}
