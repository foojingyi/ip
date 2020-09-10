package duke.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import duke.Ui;
import duke.Storage;
import duke.TaskList;
import duke.dukeexception.DukeException;
import duke.dukeexception.WrongDeadlineException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;



/**
 * Command that adds a task to the user's list when executed.
 */
public class AddCommand extends Command {
    /** Represents the type of the command used by the user */
    private CommandType commandType;
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
    public void execute(TaskList tasks, Storage storage) throws DukeException {
        Task task = null;
        if (this.commandType == CommandType.TODO) {
            task = new Todo(this.description);
        } else if (this.commandType == CommandType.DEADLINE) {
            String[] descElements = this.description.split(" /by ");
            try {
                LocalDateTime dateTime = LocalDateTime.parse(descElements[1],
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                String taskName = descElements[0];
                task = new Deadline(taskName, dateTime);
            } catch (Exception e) {
                throw new WrongDeadlineException("deadline", "/by");
            }
        } else { // should be event
            String[] descElements = this.description.split(" /at ");
            try {
                LocalDateTime dateTime = LocalDateTime.parse(descElements[1],
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                String taskName = descElements[0];
                task = new Event(taskName, dateTime);
            } catch (Exception e) {
                throw new WrongDeadlineException("event", "/at");
            }
        }

        if (task != null) {
            tasks.addTask(task, storage);

            System.out.println("Orh. I added:" + "\n  " + task.toString()
                    + "\nNow you got " + tasks.getListLength() + " things in the list.");
        }
    }

    @Override
    public String executeToGui(TaskList tasks, Storage storage, Ui ui) throws DukeException {
        Task task = null;
        if (this.commandType == CommandType.TODO) {
            task = new Todo(this.description);
        } else if (this.commandType == CommandType.DEADLINE) {
            String[] descElements = this.description.split(" /by ");
            try {
                LocalDateTime dateTime = LocalDateTime.parse(descElements[1],
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                String taskName = descElements[0];
                task = new Deadline(taskName, dateTime);
            } catch (Exception e) {
                throw new WrongDeadlineException("deadline", "/by");
            }
        } else { // should be event
            String[] descElements = this.description.split(" /at ");
            try {
                LocalDateTime dateTime = LocalDateTime.parse(descElements[1],
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                String taskName = descElements[0];
                task = new Event(taskName, dateTime);
            } catch (Exception e) {
                throw new WrongDeadlineException("event", "/at");
            }
        }

        String response = "Test from AddCommand";
        if (task != null) {
            tasks.addTask(task, storage);

            response = ui.returnReply("Orh. I added:" + "\n  " + task.toString()
                    + "\nNow you got " + tasks.getListLength() + " things in the list.");
        }
        return response;
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
