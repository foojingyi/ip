package duke.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import duke.Storage;
import duke.TaskList;
import duke.Ui;
import duke.dukeexception.DukeException;
import duke.dukeexception.WrongDateTimeException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;



/**
 * Command that adds a task to the user's list when executed.
 */
public class AddCommand extends Command {
    /** DateTime Formatter for formatting date and time from user's command String */
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    /** Represents the type of the command used by the user */
    private final CommandType commandType;
    /** Additional information needed for executing the command */
    private final String description;

    /**
     * Public constructor.
     *
     * @param commandType Either <code>TODO</code>, <code>Deadline</code>,
     *                    or <code>Event</code>.
     * @param description Name of the task (and date and time if
     *                    <code>Deadline</code> or <code>Event</code>.
     */
    public AddCommand(CommandType commandType, String description) {
        this.commandType = commandType;
        this.description = description;
    }

    @Override
    public String execute(TaskList tasks, Storage storage, Ui ui) throws DukeException {
        Task task;
        if (this.commandType == CommandType.TODO) {
            task = new Todo(this.description);
        } else if (this.commandType == CommandType.DEADLINE) {
            task = createDeadline();
        } else if (this.commandType == CommandType.EVENT) {
            task = createEvent();
        } else {
            throw new DukeException("Something went wrong! Try again.");
        }

        tasks.addTask(task, storage);

        return ui.returnReply("Orh. I added:" + "\n  " + task.toString()
                    + "\nNow you got " + tasks.getListLength() + " things in the list.");
    }

    /**
     * Creates Deadline object.
     *
     * @return Deadline object as created according to user's specifications.
     * @throws WrongDateTimeException When date and time are given in the wrong format.
     */
    private Deadline createDeadline() throws WrongDateTimeException {
        try {
            String[] descElements = this.description.split(" /by ");
            String taskName = descElements[0];
            String dateTimeString = descElements[1];
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, FORMATTER);

            return new Deadline(taskName, dateTime);
        } catch (DateTimeParseException e) {
            throw new WrongDateTimeException(this.commandType, "/by");
        }
    }

    /**
     * Creates Event object.
     *
     * @return Event object as created according to user's specifications.
     * @throws WrongDateTimeException When date and time are given in the wrong format.
     */
    private Event createEvent() throws WrongDateTimeException {
        try {
            String[] descElements = this.description.split(" /at ");
            String taskName = descElements[0];
            String dateTimeString = descElements[1];
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, FORMATTER);

            return new Event(taskName, dateTime);
        } catch (DateTimeParseException e) {
            throw new WrongDateTimeException(this.commandType, "/at");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
