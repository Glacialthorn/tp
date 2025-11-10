---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# EstateMate Developer Guide

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* The `toDisplayValue` method in the `LeaseAmount` class was adapted
  from [this StackOverflow answer](https://stackoverflow.com/a/3395845) to format currency using commas for every
  thousand digit.

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

**`Main`** (consisting of classes [
`Main`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/Main.java) and [
`MainApp`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/MainApp.java)) is in
charge of the app launch and shut down.

* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues
the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API
  `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using
the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component
through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the
implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

<div style="page-break-after: always;"></div>

### UI component

The **API** of this component is specified in [
`Ui.java`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `TenantListPanel`,
`StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures
the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that
are in the `src/main/resources/view` folder. For example, the layout of the [
`MainWindow`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/ui/MainWindow.java)
is specified in [
`MainWindow.fxml`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

<box type="info" seamless>

**Note:**

* `MainWindow` stores a reference to a `Model` interface instance. This is to pass the `Model` instance to
  `TenantListPanel` as well as to retrieve job information for `JobListPanel`.
* `TenantCard` has a stronger association with the `Model` component because it stores a reference to a `Model`
  interface instance. However, this is used only to retrieve and display jobs linked to a tenant.
  </box>

<div style="page-break-after: always;"></div>

### Logic component

**API** : [
`Logic.java`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API
call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteTenantCommandParser` should end at the destroy marker (X) but due to a limitation of
PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an **`EstateMateParser`** object which in turn
   creates a parser that matches the command (e.g., `DeleteTenantCommandParser` or `AddJobCommandParser`) and uses it to
   parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteTenantCommand`)
   which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a tenant or add a job).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take
   several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:

* When called upon to parse a user command, the **`EstateMateParser`** class creates an `XYZCommandParser` (`XYZ` is a
  placeholder for the specific command name e.g., `AddTenantCommandParser`) which uses the other classes shown above to
  parse the user command and create a `XYZCommand` object (e.g., `AddTenantCommand`) which the **`EstateMateParser`**
  returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddTenantCommandParser`, `DeleteTenantCommandParser`, ...) inherit from the
  `Parser` interface so that they can be treated similarly where possible e.g, during testing.

<div style="page-break-after: always;"></div>

### Model component

**API** : [
`Model.java`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/model/Model.java)
<puml src="diagrams/ModelClassDiagram.puml" width="450" />

The `Model` component,

* stores the EstateMate data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which
  is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to
  this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a
  `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they
  should make sense on their own without depending on other components)

### Storage component

**API** : [
`Storage.java`](https://github.com/AY2526S1-CS2103T-F08a-2/tp/blob/master/src/main/java/seedu/estatemate/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,

* can save both EstateMate data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `EstateMateStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the
  functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects
  that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.estatemate.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Job feature

#### Implementation Overview

This section explains how **Jobs** are modelled, parsed, stored, and presented.

#### Model

**Core Types**

* `Job` fields: `id:int`, `description:Description`, `isDone:boolean`.
* `Description` validates non-blank descriptions.
* `UniqueJobList` maintains all jobs; provides add/remove/mark/unmark and an unmodifiable observable view.

**Tenant References**

* `Person` keeps `List<Integer> jobs` (job **ids**).
* Deleting a job **cascades**: the job is removed and its id is stripped from all persons’ `jobs` lists.

<puml src="diagrams/JobModelClassDiagram.puml" width="360" />

#### Logic

**Commands**

* `AddJobCommand` (`job d/<desc>`) parses `Description`, allocates id via `Model#nextJobId()`, adds job.
* `EditJobCommand` (`ejob <id> d/<desc>`) updates job **description** for the given id; preserves `isDone`.
* `DeleteJobCommand` (`djob <id>`) removes job and triggers cascade unlink from tenants.
* `MarkJobCommand` / `UnmarkJobCommand` (`mark <id>` / `unmark <id>`) toggles completion.
* `FindJobCommand` (`fjob KEYWORDS…`) filters by case-insensitive keywords with `JobContainsKeywordsPredicate`.

**Parsers**

* `AddJobCommandParser`, `EditJobCommandParser`, `DeleteJobCommandParser`,
  `MarkJobCommandParser`, `UnmarkJobCommandParser`, `FindJobCommandParser`
  tokenize arguments, validate fields, and construct commands.

**Post-mutation behaviour**

* After job-list-changing mutations (job / ejob / djob), `ModelManager` resets the job filter to `PREDICATE_SHOW_ALL_JOBS`. Mark/unmark just update the job’s status and keep the current filter.

<puml src="diagrams/JobAddSequence.puml" width="900" />

#### Storage

**JSON Mapping**

* `JsonAdaptedJob` <-> `Job` with fields  
  `{ "id": number (>0), "description": string, "isDone": boolean }`.
* `JsonSerializableEstateMate` rejects duplicates by id. Jobs with the same description are allowed and will all be loaded. Load errors surface as
  `DataLoadingException`; the app then starts with an **empty** dataset (AB-3 fallback path).

**Save Flow**

* After a successful command, `LogicManager` persists via `StorageManager#saveEstateMate(...)`.

<puml src="diagrams/JobStorageDiagram.puml" width="640" />

#### UI

**Lists & Cards**

* `JobListPanel` renders the `ObservableList<Job>`.
* Each `JobCard` shows `id`, `description`, and a completion badge derived from `isDone`.
* The list updates automatically when `ModelManager`'s `filteredJobs` changes.

<puml src="diagrams/JobUiClassDiagram.puml" width="360" />

---

**Design Notes**

* **Safe startup**: duplicate jobs in storage (by id) cause load to fail; the app falls back to **empty**
  data.
* **Consistency**: job deletion removes any references from tenants (by id) to keep the model coherent.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

# **Appendix: Requirements**

## Product scope

**Target user profile**:

* Property managers and property management companies managing multiple rental units.
* Needs to track and update tenant details efficiently (contact info, lease, rent, maintenance jobs).
* Needs a reliable system to manage both tenants and property maintenance.
* Prefers a fast, keyboard-driven desktop application.
* Comfortable with structured data entry and managing records through commands.

**Value proposition**:
EstateMate helps property managers stay organized and in control of their operations.
It provides a ***centralized and efficient way*** to manage tenants, rental records, and maintenance jobs all from a
simple, keyboard-friendly interface.
With EstateMate, users can:

- Maintain accurate and organised tenant records
- Monitor rental payments and lease durations
- Track maintenance jobs and their completion status
- Improve operational efficiency through a command-driven interface

<br>

## User Stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a ...         | I want to ...                         | So that I can...                                                               |
|----------|------------------|---------------------------------------|--------------------------------------------------------------------------------|
| '***'    | Property Manager | Add new tenants and their information | manage tenants for contacting and tracking their information                   |
| '***'    | Property Manager | Delete tenants and their information  | remove people who are no longer tenants                                        |
| '***'    | Property Manager | List all tenants                      | see what tenants I am keeping track of                                         |
| '***'    | Property Manager | Add new maintenance jobs              | keep track of tasks that need to be carried out                                |
| '***'    | Property Manager | Delete maintenance jobs               | remove tasks that are no longer needed                                         |
| '***'    | Property Manager | List all jobs                         | see what jobs I am keeping track of                                            
| '***'    | Property Manager | Mark jobs as completed                | monitor the progress and ensure maintenance tasks are resolved                 |
| '***'    | Property Manager | Unmark jobs as not completed          | keep track of tasks that still need to be followed up on                       
| '***'    | Property Manager | View available commands               | refer to usage instructions when needed to avoid mistakes                      |
| '***'    | Property Manager | Exit the application                  | safely close the system and ensure all changes are saved                       |
| ---      | ---              | ---                                   | ---                                                                            |
| '**'     | Property Manager | Search a tenant                       | quickly locate a tenant without scrolling through the entire tenant list       |
| '**'     | Property Manager | Search a maintenance job              | quickly locate a maintenance job without scrolling through the entire job list 
| '**'     | Property Manager | Edit tenant details                   | update their information when changes occur                                    |
| '**'     | Property Manager | Edit maintenance jobs                 | update maintenance job description when changes occur                          
| '**'     | Property Manager | Link jobs to specific tenants         | know which tenant is associated with each maintenance request                  |
| '**'     | Property Manager | Clear all tenants from the system     | reset the tenant database or start fresh                                       

<br>

## Use Cases

<br>

(For all **use cases** below, the **System** is the 'TenantManager' and the **Actor** is the 'user', unless specified
otherwise)

---

### Use case: UC01 - Add a tenant

**MSS**

1. User requests to add a tenant with all required information.
2. **System** checks for correct parameter format and completeness.
3. **System** verifies that a tenant with the same name does not already exist.
4. **System** creates a new tenant record.
5. **System** shows the success message and refreshes the tenants list automatically.

Use case ends.

**Extensions**

* 2a. The command format is wrong (e.g., missing parameter, wrong date format).
    * 2a1. **System** shows an error message and provides the correct input format. <br>
      Use case ends.
* 3a. A tenant with the same name already exists.
    * 3a1. **System** shows an error message indicating that the tenant already exists. <br>
      Use case ends.

---

### Use case: UC02 - Delete a tenant

**MSS**

1. User requests to delete a specific tenant.
2. **System** verifies that the request is valid and corresponds to an existing tenant.
3. **System** removes the specified tenant.
4. **System** shows the success message and refreshes the list of tenants automatically.

Use case ends.

**Extensions**

* 2a. The given tenant number is invalid.
    * 2a1. **System** shows an error message that the provided tenant number is invalid and provides
      the correct input format. <br>
      Use case ends.
* 2b. The given tenant number does not correspond to any existing tenant.
    * 2b1. **System** shows an error message indicating that the tenant does not exist. <br>
      Use case ends.

---

### Use case: UC03 - List all tenants

**MSS**

1. User requests to view all tenant information.
2. **System** displays the full list of tenants, including all details for each tenant.

Use case ends.

**Extensions**

* 1a. The command format is wrong (e.g., wrong spelling).
    * 1a1. **System** shows an error message indicating that the command format is invalid. <br>
      Use case ends.

---

### Use case: UC04 - Add a maintenance job

**MSS**

1. User requests to add a maintenance job, with provided job description.
2. **System** verifies that description is provided and not empty.
3. **System** creates a new maintenance job and sets default status in the job list.
4. **System** shows the success message and refreshes the job list display to include the newly added job.

Use case ends.

**Extensions**

* 2a. The given job description is missing or empty.
    * 2a1. **System** shows an error message indicating that the description cannot be empty. <br>
      Use case ends.

---

### Use case: UC05 - Delete a maintenance job

**MSS**

1. User requests to delete a maintenance job.
2. **System** verifies that the request is valid and corresponds to an existing job.
3. **System** deletes the specified maintenance job.
4. **System** shows the success message and refreshes the job list to reflect the removal.

Use case ends.

**Extensions**

* 2a. The given input is invalid (e.g., empty, not positive integer).
    * 2a1. **System** shows an error message that the provided input is invalid and provides
      the correct input format. <br>
      Use case ends.
* 2b. The given input does not correspond to any existing job.
    * 2b1. **System** shows an error message indicating that the job does not exist. <br>
      Use case ends.

---

### Use case: UC06 - List all maintenance jobs

**MSS**

1. User requests to view all maintenance jobs.
2. **System** displays the full list of maintenance jobs, including the completion status of each job.

Use case ends.

**Extensions**

* 1a. The command format is wrong (e.g., wrong spelling).
    * 1a1. **System** shows an error message indicating that the command format is invalid. <br>
      Use case ends.

---

### Use case: UC07 - Mark maintenance job

**MSS**

1. User requests to mark a maintenance job as completed.
2. **System** verifies that the request is valid and corresponds to an existing unmarked job.
3. **System** sets the status of the maintenance job.
4. **System** shows the success message and refreshes the maintenance job list display
   to reflect the new status.

Use case ends.

**Extensions**

* 2a. The given input is invalid (e.g., empty, not positive integer).
    * 2a1. **System** shows an error message that the provided input is invalid and provides
      the correct input format. <br>
      Use case ends.
* 2b. The given input does not correspond to any existing job in the job list
    * 2b1. **System** shows an error message indicating that the job does not exist. <br>
      Use case ends.
* 2c. The job is already marked as completed.
    * 2c1. **System** shows a message indicating that the job is already completed. <br>
      Use case ends.

---

### Use case: UC08 - Unmark maintenance job

**MSS**

1. User requests to revert a maintenance job as not completed.
2. **System** verifies that the request is valid and corresponds to an existing marked job.
3. **System** sets the status of the maintenance job accordingly in the job list in any linked
   tenant's job list.
4. **System** shows the success message and refreshes the maintenance job list display to reflect the new status.

Use case ends.

**Extensions**

* 2a. The given input is invalid (e.g., empty, not positive integer).
    * 2a1. **System** shows an error message that the provided input is invalid and provides
      the correct input format. <br>
      Use case ends.
* 2b. The given input does not correspond to any existing job in the job list.
    * 2b1. **System** shows an error message indicating that the job does not exist. <br>
      Use case ends.
* 2c. The job is already marked as not completed.
    * 2c1. **System** shows a message indicating that the job is already in pending state. <br>
      Use case ends.

---

### Use case: UC09 - Help command

**MSS**

1. User requests a list of all available commands.
2. **System** directs user to the user guide.

Use case ends.

**Extensions**

* 1a. The command format is wrong (e.g., wrong spelling).
    * 1a1. **System** shows an error message indicating that the command format is invalid. <br>
      Use case ends.

---

### Use case: UC10 - Find a tenant

**MSS**

1. User requests to find a specific tenant.
2. **System** verifies the entered keyword(s).
3. **System** retrieves all matching tenant records.
4. **System** automatically displays the list of matching tenants along with their keywords.

Use case ends.

**Extensions**

* 2a. The given keyword(s) is invalid (e.g., empty).
    * 2a1. **System** shows an error message indicating that the provided keyword(s) is invalid. <br>
      Use case ends.
* 3a. No tenants match the search keyword.
    * 3a1. **System** shows a message indicating that no matching tenants were found. <br>
      Use case ends.

---

### Use case: UC11 - Find a maintenance job

**MSS**

1. User requests to find a maintenance job.
2. **System** verifies the given keyword(s).
3. **System** retrieves all matching job records.
4. **System** automatically displays the list of matching jobs.

**Extensions**

* 2a. The given keyword(s) is invalid (e.g., empty).
    * 2a1. **System** shows an error message indicating that the provided keyword(s) is invalid. <br>
      Use case ends.
* 3a. No job matches the search keyword(s).
    * 3a1. **System** shows a message indicating that no matching jobs were found. <br>
      Use case ends.

---

### Use case: UC12 - Edit a tenant

**MSS**

1. User requests to edit a tenant with one or more updated fields.
2. **System** verifies that the request is valid and corresponds to an existing tenant.
3. **System** updates only the specified fields of the tenant's record while keeping other details unchanged.
4. **System** shows the success message and refreshes the tenant list display to reflect the changes.

Use case ends.

**Extensions**

* 2a. The given input is invalid (e.g., empty, not positive integer).
    * 2a1. **System** shows an error message that the provided input is invalid and provides
      the correct input format. <br>
      Use case ends.
* 2b. The given input does not correspond to any existing tenant in the list.
    * 2b1. **System** shows an error message indicating that the tenant does not exist. <br>
      Use case ends.
* 2c. The command does not include any field to edit.
    * 2c1. **System** shows an error message indicating that at least one field must be specified. <br>
      Use case ends.

---

### Use case: UC13 - Edit a maintenance job

**MSS**

1. User requests to edit a maintenance job with updated description.
2. **System** verifies that the request is valid and corresponds to an existing job.
3. **System** updates only the maintenance job description.
4. **System** shows the success message and refreshes the job list display to reflect the changes.

Use case ends.

**Extensions**

* 2a. The given input is invalid (e.g., empty, not positive integer).
    * 2a1. **System** shows an error message that the provided input is invalid and provides
      the correct input format. <br>
      Use case ends.
* 2b. The given input does not correspond to any existing job in the list.
    * 2b1. **System** shows an error message indicating that the job does not exist. <br>
      Use case ends.
* 2c. The command does not include any description to edit.
    * 2c1. **System** shows an error message indicating that description cannot be empty. <br>
      Use case ends.

---

### Use case: UC14 - Link maintenance job to tenant

**MSS**

1. User requests to link an existing maintenance job to a tenant.
2. **System** verifies that the request is valid, corresponds to an existing tenant and maintenance job.
3. **System** links the specified job to the tenant updating the tenant's assigned job list and maintaining the job
   status.
4. **System** shows a success message and refreshes the UI to reflect the linked job under the tenant's assigned jobs.

Use case ends.

**Extensions**

* 2a. The given input is invalid (e.g., empty fields, not positive integer).
    * 2a1. **System** shows an error message that the provided input is invalid and
      provides the correct input format. <br>
      Use case ends.
* 2b. The given tenant number does not correspond to any existing tenant.
    * 2b1. **System** shows an error message indicating that the tenant does not exist. <br>
      Use case ends.
* 2c. The given input does not correspond to any existing job.
    * 2c1. **System** shows an error message indicating that the job does not exist. <br>
      Use case ends.
* 2d. The job is already linked to tenant.
    * 2d1. **System** shows a message indicating that the job is already assigned to the tenant. <br>
      Use case ends.

---
<div style="page-break-after: always;"></div>

### Use case: UC15 - Clear

**MSS**

1. User removes all tenant contacts in the database.
2. EstateMate clears the database and updates local JSON file.

Use case ends.

---

### Use case: UC16 - Exit

**MSS**

1. User wants to exit the application.
2. EstateMate is terminated.

Use case ends.

---

## Non-Functional Requirements

### Performance

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be packaged from a single JAR file.
3. Should not take up more than 100MB in file size.
4. Should be able to hold up to 1000 contacts without a noticeable sluggishness in performance for typical usage.

> **Note:** within 1 second for typical operations like listing, searching, adding, etc.

5. Users with above-average typing speed should be able to perform most tasks **faster via commands** than using a
   mouse.
6. Should work without requiring an installer.
7. Should work well for standard screen resolutions and be usable for resolutions 1280x720, screen scales 150%.

> **Note:** Standard screen resolutions - 1920x1080 and higher, screen scales 100% and 125%.

### Usability

1. The system should provide **clear, informative error messages to guide user** for invalid inputs.
2. Commands and outputs should follow a consistent format to minimize confusion.
3. The data should be stored locally and be in a human editable text file.

### Reliability

1. Data should not be lost in case of sudden termination.
2. The system should handle invalid inputs without crashing or corrupting.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file `estatemate.jar` and copy into an empty folder

    2. `cd` to the folder with the jar file and running the command `java -jar estatemate.jar` to launch the app <br>
       Expected: Shows the GUI with a set of sample contacts. The window size may not be optimal.

2. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. Proper shutdown

    1. Open the help window by entering `help` into the command box or using the button at the top left,

    2. Close the main application window. <br>
       Expected: Both the help window and the main window should close.

### Deleting a tenant

1. Deleting a tenant while all tenants are being shown

    1. Prerequisites: List all tenants using the `list` command. Multiple tenants in the list.

    2. Test case: `delete 1`<br>
       **Expected:** First tenant is deleted from the list. Details of the deleted tenant shown in the status message.

    3. Test case: `delete 0`<br>
       **Expected:** No person is deleted. Error details shown in the status message. Tenant list remains the same.

    4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       **Expected:** Similar to previous.

### Adding a job

1. Adding a new job
    1. Prerequisites: No specific prerequisites.

    2. Test case: `job d/ Pest infestation` <br>
       **Expected:** Job is added to the job list. Details of the job are shown in the status message. Switch to a job
       list
       screen where the added job can be seen

### Finding tenants

1. Finding tenants by name
    1. Prerequisites: At least one tenant with name containing the word `Alex`, and no tenant with name containing the
       word `Michael`.

    2. Test case: `find alex` <br>
       **Expected:** Only tenants with names containing the word `Alex` (non-case sensitive) are displayed.

    3. Test case: `find michael` <br>
       **Expected:** An empty tenant list is displayed.

### Finding jobs

1. Finding jobs by description
    1. Prerequisites: At least one job with name containing the word `pest`, and no job with name containing the word
       `pipe`.

    2. Test case: `fjob pest` <br>
       **Expected:** Only jobs with descriptions containing the word `pest` (non-case sensitive) are displayed.

    3. Test case: `fjob pipe` <br>
       **Expected:** An empty job list is displayed.

### Linking jobs to a tenant

1. Linking an existing tenant and job to each other
    1. Prerequisites: List all tenants using the `list` command. At least one tenant in the list. List all jobs using
       the `ljob` command. At least one job (job id: 1) in the list.

    2. Test case: `link 1 j/1`
       **Expected:** Job is linked to the tenant. Status message shows the job number and name of tenant linked. Job is
       displayed in the tenant's maintenance information section.

2. Linking a nonexistent tenant and a job to each other.
    1. Prerequisites: List all tenants using the `list` command. At most 4 tenants in the list. List all jobs using the
       `ljob` command. At least one job (job id: 1) in the list.

    2. Test case: `link 5 j/1`
       **Expected:** Job is not linked to the tenant. Error details shown in status message indicating invalid tenant
       index.
3. Linking a tenant and a nonexisting job to each other.
    1. Prerequisites: List all tenants using the `list` command. at least 1 tenant in the list. List all jobs using the
       `ljob` command. At most 3 (max job id: 3) in the list.

    2. Test case: `link 1 j/4`
       **Expected:** Job is not linked to the tenant. Error details showing in status message indicating invalid job
       index.

## **Appendix: Planned Enhancements**

Team Size: 5 <br>

1. Implement split screen display. (Allow both job lists and tenant lists to be displayed at the same time)
2. Allow tenant names to support special/non-ASCII characters. (Not just alphanumeric characters and spaces)
   - Allow for valid names like `Anne-marie`, `O'Brien`, `José` .
3. Improve tenant duplicate detection to include consideration for phone numbers.
   - Allows same name, as long as the phone number is different.
4. `help` command to show a quick, brief reference to available commands without the need to access external links.
5. Enhance `find` command to no longer require full words to match.
   - e.g. `find Ale` will display the tenant `Alex Yeoh`.
6. Enhance `fjob` command to no longer require full words to match.
   - e.g. `fjob leak` will display the job with description `Pipe leakage`.
7. Add a command to unlink a job from a tenant. (Reverse of existing `link` command)
8. Perform more refined parsing validation for the `tenant` command, specifically involving the `a/` tag. 
   - Currently, an address field of `a/ a/` is not accepted, while one of `a/a/` is accepted. 
9. Perform more rigorous validation of special character positions in phone numbers in the `tenant` command
    - e.g. Phone numbers like `999(())9999` or `99-----9` should be rejected.
10. Add an extra layer of user verification to the `clear` command.
    - e.g. Require user to input 'Y' in response to a verification question before clearing the data.

## **Appendix: Effort**

The effort required and difficulty level was high, as we generally did not reuse any code (except for 1 method adapted
from StackOverflow). <br>
While AB3 deals with only one entity type (Persons), EstateMate needs to handle two (Tenants and Jobs). The tenant class
is also much more detailed with more fields compared to the Person class from AB3.

### Project Achievements

1. **Adding jobs and their related functionality** <br>
   Challenges:
    1. Finding a way to implement the storage for job data without interfering with the existing data storage for
       tenants.
    2. Switching to non-index based commands for jobs as having both index based commands for tenants and jobs could
       cause issues.
    3. Creating a whole new GUI section and components for displaying of jobs separately from tenants.
       <br>
2. **Adding additional fields to the existing `add` command to allow for adding tenants** <br>
   Challenges:
    1. Performing date parsing and verification, as Tenant has fields requiring dates while Person from AB3 does not.
       There are many edge cases to consider and thus this aspect is error-prone.
    2. Adding job-related methods and fields to the model for tenants while maintaining high cohesion simultaneously.
       <br>
3. **Adding a new GUI screen to accommodate the addition of jobs** <br>
   Challenges:
    1. Starting from scratch as AB3 only has one screen to manage, while we have two.
    2. Managing the switching between screens based on commands, which we also needed to implement from scratch.
