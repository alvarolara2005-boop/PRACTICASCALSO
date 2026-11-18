Feature: Register a training proposal for a scheduled session

  Scenario: BDD-01a - Register a proposal that fits the selected session
    Given dojo "DOJO-01" has the scheduled session "SES-001"
    When the sensei registers proposal "PROP-001" of 45 minutes for session "SES-001"
    Then the proposal is accepted
    And proposal "PROP-001" can be retrieved with 45 minutes for dojo "DOJO-01" and session "SES-001"