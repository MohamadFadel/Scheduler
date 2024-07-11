
Feature: Task APIs

  Background:
    Given I have the following tasks
      | taskType    | description   | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                                                                        |
      | Auto-Order  | Cucumber Test | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | GMT+1                       | 4                                 | 0                                | monthly                      | auto.scheduser@outlook.com,user.scheduler@card.com,auto.order_sched@gmail.com |
    When I schedule them with given Ids
      | testTaskId1 |

  Scenario: Updating a task with given Id
    Given I have the following task
      | taskType    | description           | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                             |
      | Auto-Order  | Updated Cucumber Test | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | GMT+1                       | 4                                 | 0                                | monthly                      | updated.auto.scheduser@outlook.com |
    When I update it with given Id
      | testTaskId1 |
    Then it should be updated correctly
    And an error should not be returned

  Scenario: Pausing a task with given Id
    When I pause a task with given Id
      | testTaskId1 |
    Then it should be paused correctly
    And an error should not be returned

  Scenario: Resuming a paused task
    When I pause a task with given Id
      | testTaskId1 |
    And I resume a task with given Id
      | testTaskId1 |
    Then it should be resumed correctly
    And an error should not be returned

  Scenario: Deleting an existing task
    When I delete a task with given Id
      | testTaskId1 |
    Then it should be deleted correctly
    And an error should not be returned