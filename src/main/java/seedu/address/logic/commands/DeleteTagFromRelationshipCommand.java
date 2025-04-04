package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_USERID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.relationship.Relationship;
import seedu.address.model.tag.Tag;

/**
 * Deletes a tag from a relationship between two persons in the address book.
 * <p>
 * The relationship is identified by the user IDs of the two persons and the name of the relationship.
 * The tag is identified by its name.
 * </p>
 */
public class DeleteTagFromRelationshipCommand extends Command {
    public static final String COMMAND_WORD = "deleteRelationshipTag";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes a tag from a relationship between two persons.\n"
            + "Parameters: "
            + PREFIX_USERID + "USER_ID_1 "
            + PREFIX_USERID + "USER_ID_2 "
            + PREFIX_NAME + "RELATIONSHIP_NAME "
            + PREFIX_TAG + "TAG\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_USERID + "12345678 "
            + PREFIX_USERID + "87654321 "
            + PREFIX_NAME + "Business Partner "
            + PREFIX_TAG + "Important" + "\n"
            + "Note: You can use either the forward or reverse relationship name to identify the relationship.\n";

    public static final String MESSAGE_SUCCESS = "Removed tag from relationship: %1$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "This tag does not exist in the relationship";
    public static final String MESSAGE_RELATIONSHIP_NOT_FOUND = "The specified relationship was not found";

    private final String userId1;
    private final String userId2;
    private final String relationshipName;
    private final Tag tag;

    /**
     * Creates a {@code DeleteTagFromRelationshipCommand} to delete the specified tag from the relationship between two
     * persons.
     *
     * @param userId1 The user ID of the first person in the relationship.
     * @param userId2 The user ID of the second person in the relationship.
     * @param relationshipName The name of the relationship.
     * @param tag The tag to delete.
     */
    public DeleteTagFromRelationshipCommand(String userId1, String userId2, String relationshipName, Tag tag) {
        requireNonNull(userId1);
        requireNonNull(userId2);
        requireNonNull(relationshipName);
        requireNonNull(tag);

        this.userId1 = userId1;
        this.userId2 = userId2;
        this.relationshipName = relationshipName;
        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Relationship relationship = model.getRelationship(userId1, userId2, relationshipName);
        if (relationship == null) {
            throw new CommandException(MESSAGE_RELATIONSHIP_NOT_FOUND);
        }

        if (!relationship.getTags().contains(tag)) {
            throw new CommandException(MESSAGE_TAG_NOT_FOUND);
        }

        Relationship updatedRelationship = relationship.withRemovedTag(tag);
        model.updateRelationship(relationship, updatedRelationship);

        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedRelationship));
    }
}
