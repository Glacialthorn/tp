package seedu.estatemate.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.estatemate.model.job.Job;

/**
 * An UI component that displays information of a {@code Job}.
 */
public class JobCard extends UiPart<Region> {

    private static final String FXML = "JobCard.fxml";

    public final Job job;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label description;
    @FXML
    private Label status;

    /**
     * Creates a {@code JobCard} with the given {@code Job} and index to display.
     * It highlights the card based on the job's completion status.
     */
    public JobCard(Job job, int displayedIndex) {
        super(FXML);
        this.job = job;

        id.setText(displayedIndex + ". ");
        description.setText(job.getDescription().toString());
        id.setText(job.getId() + ". ");

        if (job.getDone()) {
            status.setText("Status: Completed ✅");
            status.getStyleClass().add("job-status-completed");
        } else {
            status.setText("Status: Pending ❌");
            status.getStyleClass().add("job-status-pending");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof JobCard)) {
            return false;
        }

        JobCard otherJobCard = (JobCard) other;
        return id.getText().equals(otherJobCard.id.getText())
                && job.equals(otherJobCard.job);
    }
}
