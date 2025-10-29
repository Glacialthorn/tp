---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# EstateMate Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* The `toDisplayValue` method in the `LeaseAmount` class was adapted from [this StackOverflow answer](https://stackoverflow.com/a/3395845) to format currency using commas for every thousand digit.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `TenantListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

<box type="info" seamless>

**Note:**
* `MainWindow` stores a reference to a `Model` interface instance. This is to pass the `Model` instance to `TenantListPanel` as well as to retrieve job information for `JobListPanel`. 
* `TenantCard` has a stronger association with the `Model` component because it stores a reference to a `Model` interface instance. However, this is used only retrieve and display jobs linked to a tenant.
</box>

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("dtenant 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `dtenant 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteTenantCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteTenantCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteTenantCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddTenantCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddTenantCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddTenantCommandParser`, `DeleteTenantCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `EstateMateStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.estatemate.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Job feature

#### Implementation Overview

This section explains how **Jobs** are modelled, parsed, stored, and presented.

#### Model

- **Core Types**
    - `Job` fields: `id:int`, `description:Description`, `isDone:boolean`.
    - `Description` validates non-blank descriptions.
    - `UniqueJobList` maintains all jobs; guarantees **uniqueness by description** via `Job#isSameJob`. Provides add/remove/mark/unmark and an unmodifiable observable view.

- **Ownership & Access**
    - `EstateMate` owns a `UniqueJobList` and provides:
        - `addJob(Job)`
        - `removeJobById(int)`
        - `markJobById(int)`
        - `unmarkJobById(int)`
        - `getJobList()`
        - `nextJobId()` *(computed as `max(id)+1`, defaulting to `1`)*
    - `ModelManager` forwards these model operations and exposes:
        - `addJob(Job)`
        - `deleteJobById(int)`
        - `editJobById(int, Description)`
        - `markJobById(int)`
        - `unmarkJobById(int)`
        - `getFilteredJobList()`
        - `updateFilteredJobList(Predicate<Job>)`
        - `nextJobId()`
        - `getJobDescriptionById(int)`
        - `isJobCompleted(int)`
        - `hasJobWithDescription(Description)`


- **Tenant References**
    - `Person` keeps `List<Integer> jobs` (job **ids**).  
      Deleting a job **cascades**: the job is removed and its id is stripped from all persons’ `jobs` lists.

<puml src="diagrams/JobModelClassDiagram.puml" width="720" />

#### Logic

- **Commands**
    - `AddJobCommand` (`job d/<desc>`) parses `Description`, allocates id via `Model#nextJobId()`, adds job.
    - `EditJobCommand` (`ejob <id> d/<desc>`) updates job **description** for the given id; preserves `isDone`.
    - `DeleteJobCommand` (`djob <id>`) removes job and triggers cascade unlink from tenants.
    - `MarkJobCommand` / `UnmarkJobCommand` (`mark <id>` / `unmark <id>`) toggles completion.
    - `FindJobCommand` (`fjob KEYWORDS…`) filters by case-insensitive keywords with `JobContainsKeywordsPredicate`.

- **Parsers**
    - `AddJobCommandParser`, `EditJobCommandParser`, `DeleteJobCommandParser`,
      `MarkJobCommandParser`, `UnmarkJobCommandParser`, `FindJobCommandParser`
      tokenize arguments, validate fields, and construct commands.

- **Post-mutation behaviour**
    - After any mutation, `ModelManager` resets the filter to `PREDICATE_SHOW_ALL_JOBS`.

<puml src="diagrams/JobAddSequence.puml" width="720" />

#### Storage

- **JSON Mapping**
    - `JsonAdaptedJob` <-> `Job` with fields  
      `{ "id": number (>0), "description": string, "isDone": boolean }`.
    - `JsonSerializableEstateMate` rejects duplicates **by id** or **by description**. Load errors surface as `DataLoadingException`; the app then starts with an **empty** dataset (AB-3 fallback path).

- **Save Flow**
    - After a successful command, `LogicManager` persists via `StorageManager#saveEstateMate(...)`.

<puml src="diagrams/JobStorageDiagram.puml" width="640" />

#### UI

- **Lists & Cards**
    - `JobListPanel` renders the `ObservableList<Job>`.
    - Each `JobCard` shows index, `id`, `description`, and a completion badge derived from `isDone`.
    - The list updates automatically when `ModelManager`'s `filteredJobs` changes.

<puml src="diagrams/JobUiClassDiagram.puml" width="680" />

---

**Design Notes**

- **Safe startup**: duplicate jobs in storage (by id or description) cause load to fail; the app falls back to **empty** data.
- **Consistency**: job deletion removes any references from tenants (by id) to keep the model coherent.


--------------------------------------------------------------------------------------------------------------------

## **Proposed features**

This section describes some noteworthy proposed features that may be implemented in the future.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `dtenant 5` command to delete the 5th person in the address book. The `dtenant` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `dtenant 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `tenant n/David …​` to add a new person. The `tenant` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `tenant n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `dtenant`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of contacts
* needs to track details regarding managing properties along with contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:
Provides easy management of tenant information for multi-property portfolios.
Captures tenant rent submissions, lease dates, complaints and maintenance scheduling.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a ... | I want to ... | So that I can...|
|---|---|---|---|
| '***' | Property Manager | Add tenants and their info | manage tenants for contacting and tracking their info |
| '***' | Property Manager | Delete tenants and their info | remove people who are no longer tenants |
| '***' | Property Manager | List all tenants | see what tenants I am keeping track of |
| '***' | Property Manager | View all available commands easily | refer to proper syntax and command usage should I forget any |
| '***' | Property Manager | Manage maintenance jobs and their status (requested/ongoing/completed) | keep track of the status of maintenance jobs |
| '***' | Property Manager | Track rent information, due dates | keep track of whose rent is due and make collections on time |
|---|---|---|---|
| '**' | Property Manager | Add notes for extra tenant information | easily be up to speed about the tenant's general circumstances when they require anything |
| '**' | Property Manager | Search a contact of tenant by name (without the need to type in full name) | I can easily search for the tenant in a long list |
| '**' | Property Manager | Edit the details of the tenants | make changes to tenants' details easily ie. when they change phone numbers |
| '**' | Property Manager | Assign issues to tenants that need to have them resolved | so that I know what outstanding issues there are and to whom they concern |
| '**' | New User | Bulk-import properties/units/tenants from CSV | onboarding is fast |
| '**' | Property Manager | Tag units with attributes (furnished, pet-friendly, aircon type) | I can filter and assign tenants appropriately based on their needs |
| '**' | Property Manager | Record payments by each tenant | so that I don't miss payments |
| '**' | Property Manager | Export/archive stored data | easily continue work on a different device or Start over with a clean slate |
| '**' | Property Manager | Get confirmation before performing irreversible operations | avoid causing any irreversible damage by accidental command usage |
| '**' | Property Manager | Add a starred contact/frequently contact list | perform my operations as quickly and efficiently as possible |
| '**' | Property Manager | Use short form commands instead of typing them fully | perform my operations as quickly and efficiently as possible |
*{More to be added}*

### Use cases
# Use Cases

(For all **use cases** below, the **System** is the 'TenantManager' and the **Actor** is the 'user', unless specified otherwise)

---

## Use case: Add tenants

**MSS**
1. User requests to add a tenant with all required information.
2. **System** checks for correct parameter format and completeness.
3. **System** verifies that a tenant with the same name does not already exist.
4. **System** creates a new tenant record and assigns a unique **Tenant ID**.
5. **System** shows the "Tenant added" success message and refreshes the tenants list.

Use case ends.

**Extensions**
* 2a. The command format is wrong (e.g., missing parameter, wrong date format).
    * 2a1. **System** shows an error message (e.g., `"Name cannot be empty"` or `"Wrong format, the correct format is <correct_format>"`).
      Use case ends.
* 3a. A tenant with the same name already exists.
    * 3a1. **System** shows the error message: `"Tenant with the same name already exists"`.
      Use case ends.

---

## Use case: Delete tenants

**MSS**
1. User requests to delete a tenant by specifying a **Tenant ID**.
2. **System** verifies that the provided **Tenant ID** is an integer and corresponds to an existing tenant.
3. **System** deletes the tenant record associated with the **Tenant ID**.
4. **System** shows the success message: `"Deleted tenant <Tenant ID> successfully."`
5. **System** refreshes the list of tenants.

Use case ends.

**Extensions**
* 2a. The provided **Tenant ID** is not a positive integer or does not correspond to an existing tenant.
    * 2a1. **System** shows the error message: `"Invalid tenant ID detected. Unable to delete tenant."`
      Use case ends.

---

## Use case: Add maintenance job

**MSS**
1. User requests to add a maintenance job, providing the unit **ADDRESS** and job **DESCRIPTION**.
2. **System** checks that both **ADDRESS** and **DESCRIPTION** are not empty.
3. **System** verifies if a maintenance job with the exact same **ADDRESS** and **DESCRIPTION** already exists (duplicate check).
4. **System** assigns a unique **Job Number** to the new job and sets its status to "Not Completed".
5. **System** shows the success message and updates the GUI with the new maintenance job.

Use case ends.

**Extensions**
* 2a. The **ADDRESS** is empty.
    * 2a1. **System** shows the error message: `"Address cannot be empty."`
      Use case ends.
* 2b. The **DESCRIPTION** is empty.
    * 2b1. **System** shows the error message: `"Description cannot be empty."`
      Use case ends.
* 3a. A duplicate maintenance job (same **ADDRESS** and same **DESCRIPTION**) is found.
    * 3a1. **System** shows an error message indicating a duplicate.
      Use case ends.

---

## Use case: Delete maintenance job for a unit

**MSS**
1. User requests to delete a maintenance job by specifying a **JOB NUMBER**.
2. **System** verifies that the **JOB NUMBER** is an integer and corresponds to an existing job in the list.
3. **System** deletes the maintenance job.
4. **System** shows the success message: `"[JOB DESCRIPTION] was successfully deleted!"`
5. **System** updates the list of maintenance jobs by removing the entry and shifting subsequent tasks up.

Use case ends.

**Extensions**
* 2a. The **JOB NUMBER** is empty.
    * 2a1. **System** shows the error message: `"Job number cannot be empty."`
      Use case ends.
* 2b. The **JOB NUMBER** is invalid (e.g., not an integer, or does not exist).
    * 2b1. **System** shows the error message: `"Job failed to be deleted. Please enter an existing job number!"` or `"Job failed to be deleted. Please enter a valid job number!"`
      Use case ends.

---

## Use case: Mark maintenance job

**MSS**
1. User requests to mark a maintenance job as done by providing the command `mark` followed by the **job number**.
2. **System** verifies that the **job number** is an integer and corresponds to an existing, unmarked job.
3. **System** sets the status of the maintenance job to "Completed" (or similar).
4. **System** shows the success message: `"[Job number] was successfully marked as done!"`
5. **System** updates the UI to display the new status of the job.

Use case ends.

**Extensions**
* 2a. The **job number** is empty.
    * 2a1. **System** shows the error message: `"Job number cannot be empty."`
      Use case ends.
* 2b. The **job number** is invalid (e.g., not an integer, or does not exist).
    * 2b1. **System** shows an error message: `"Job failed to be marked. Please enter a valid job number!"` or `"Job failed to be marked. Please enter an existing job number!"`
      Use case ends.
* 2c. The job is already marked as done.
    * 2c1. **System** shows a message indicating the job is already completed.
      Use case ends.

---

## Use case: List all tenants

**MSS**
1. User requests to view all tenant information using the `list` command.
2. **System** retrieves all existing tenant records.
3. **System** displays the full list of tenants, including all details for each tenant.

Use case ends.

**Extensions**
* 1a. The command contains errors (e.g., wrong spelling).
    * 1a1. **System** shows the error message: `"Unknown command"`.
      Use case ends.
* 2a. No tenants exist in the **System**.
    * 2a1. **System** displays a message indicating that the list is empty.
      Use case ends.

---

## Use case: Help command

**MSS**
1. User requests a list of all available commands using the `help` command.
2. **System** retrieves the list and formats it.
3. **System** displays the list of "All available commands" including their formats.

Use case ends.

**Extensions**
* 1a. The command contains errors (e.g., wrong spelling).
    * 1a1. **System** shows the error message: `"Unknown command"`.
      Use case ends.

*{More to be added}*

## Non-Functional Requirements

### Performance
1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 contacts without a noticeable sluggishness in performance for typical usage.
> **Note:** within 1 second for typical operations like listing, searching, adding, etc.
3.  Users with above-average typing speed should be able to perform most tasks **faster via commands** than using a mouse.

### Usability
1. The system should provide **clear, informative error messagaes to guide user** for invalid inputs.
2. Commands and outputs should follow a consistent format to minimize confusion.

### Reliability
1. Data should not be lost in case of sudden termination.
2. The system should handle invalid inputs without crashing or corrupting.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `dtenant 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `dtenant 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect dtenant commands to try: `dtenant`, `dtenant x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
