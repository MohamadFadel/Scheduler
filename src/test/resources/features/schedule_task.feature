
Feature: Schedule Task

  Scenario: Scheduling a task
    Given I have the following tasks
      | taskType    | description   | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                                                                        |
      | Auto-Order  | Cucumber Test | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | GMT+1                       | 4                                 | 0                                | monthly                      | auto.scheduser@outlook.com,user.scheduler@card.com,auto.order_sched@gmail.com |
    When I schedule them
    Then they should be created correctly
    And an error should not be returned

  Scenario: Scheduling a task with long description
    Given I have the following task
      | taskType   | description                                                                                                                                                                                                                                                      | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                                                                        |
      | Auto-Order | xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | GMT+1                       | 4                                 | 0                                | monthly                      | auto.scheduser@outlook.com,user.scheduler@card.com,auto.order_sched@gmail.com |
    When I schedule it
    Then an error should be returned

  Scenario: Scheduling a task with invalid task type
    Given I have the following task
      | taskType | description   | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                                                                        |
      | Test     | Cucumber Test | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | GMT+1                       | 4                                 | 0                                | monthly                      | auto.scheduser@outlook.com,user.scheduler@card.com,auto.order_sched@gmail.com |
    When I schedule it
    Then an error should be returned

  Scenario: Scheduling a task with invalid frequency
    Given I have the following task
      | taskType   | description   | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                                                                        |
      | Auto-Order | Cucumber Test | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | GMT+1                       | 4                                 | 0                                | test                         | auto.scheduser@outlook.com,user.scheduler@card.com,auto.order_sched@gmail.com |
    When I schedule it
    Then an error should be returned

  Scenario: Scheduling a task with invalid occurrence
    Given I have the following task
      | taskType   | description   | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                                                                        |
      | Auto-Order | Cucumber Test | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | GMT+1                       | -1                                | -1                               | monthly                      | auto.scheduser@outlook.com,user.scheduler@card.com,auto.order_sched@gmail.com |
    When I schedule it
    Then an error should be returned

  Scenario: Scheduling a task with invalid timezone
    Given I have the following task
      | taskType   | description   | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                                                                        |
      | Auto-Order | Cucumber Test | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | Test                        | 4                                 | 0                                | monthly                      | auto.scheduser@outlook.com,user.scheduler@card.com,auto.order_sched@gmail.com |
    When I schedule it
    Then an error should be returned

  Scenario: Scheduling a task with invalid emails
    Given I have the following task
      | taskType   | description   | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                           |
      | Auto-Order | Cucumber Test | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | GMT+1                       | 4                                 | 0                                | monthly                      | auto.scheduser@outlook.com, test |
    When I schedule it
    Then an error should be returned

  Scenario: Scheduling duplicate jobs
    Given I have the following task
      | taskType   | description   | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                                                                        |
      | Auto-Order | Cucumber Test | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | GMT+1                       | 4                                 | 0                                | monthly                      | auto.scheduser@outlook.com,user.scheduler@card.com,auto.order_sched@gmail.com |
    When I schedule it
    And I reschedule it
    Then an error should be returned