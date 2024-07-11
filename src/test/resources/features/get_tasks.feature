# Task description needs to be 'Cucumber Test'

Feature: Get Scheduled Tasks APIs

  Scenario: Department without scheduled tasks
    When I call get tasks api
    Then 0 tasks should be returned

  Scenario: Department with scheduled tasks
    Given I have the following tasks
      | taskType    | description   | bodyParam                                                                          | taskFrequencyInput.hours[0] | taskFrequencyInput.months[0] | taskFrequencyInput.minutes[0] | taskFrequencyInput.timezone | taskFrequencyInput.daysOfMonth[0] | taskFrequencyInput.daysOfWeek[0] | taskFrequencyInput.frequency | emails                           |
      | Auto-Order  | Cucumber Test | {"includeMissing":true,"includeScannedToCart":true,"orderReplacements30Days":true} | 10                          | 0                            | 30                            | GMT+2                       | 4                                 | 0                                | monthly                      | mohamad.fadel@cardinalhealth.com |
    When I schedule them with given Ids
      | testTaskId1 |
    And I call get tasks api
    Then 1 tasks should be returned
    And an error should not be returned