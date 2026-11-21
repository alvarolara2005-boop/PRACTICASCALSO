Feature: Register a training proposal for a scheduled session

  Scenario: BDD-01a - Register a proposal that fits the selected session
    Given dojo "DOJO-01" has the scheduled session "SES-001"
    When the sensei registers proposal "PROP-001" of 45 minutes for session "SES-001"
    Then the proposal is accepted
    And proposal "PROP-001" can be retrieved with 45 minutes for dojo "DOJO-01" and session "SES-001"

  Scenario Outline: <id> - Register a proposal within the allowed duration
    Given dojo "DOJO-01" has the scheduled session "SES-001"
    When the sensei registers proposal "<proposal>" of <minutes> minutes for session "SES-001"
    Then the proposal is accepted
    And proposal "<proposal>" can be retrieved with <minutes> minutes for dojo "DOJO-01" and session "SES-001"

    Examples:
      | id      | proposal | minutes |
      | BDD-01b | PROP-004 | 59      |
      | BDD-01c | PROP-005 | 60      |
      | BDD-01d | PROP-006 | 1       |

  Scenario Outline: <id> - Reject a proposal that exceeds the session duration
    Given dojo "DOJO-01" has the scheduled session "SES-001"
    When the sensei registers proposal "<proposal>" of <minutes> minutes for session "SES-001"
    Then the proposal is rejected with reason "PROPOSAL_EXCEEDS_AVAILABLE_DURATION"
    And proposal "<proposal>" is not registered

    Examples:
      | id      | proposal | minutes |
      | BDD-02  | PROP-002 | 75      |
      | BDD-02b | PROP-007 | 61      |

  Scenario: BDD-03 - Reject a proposal with duration zero
    Given dojo "DOJO-01" has the scheduled session "SES-001"
    When the sensei registers proposal "PROP-003" of 0 minutes for session "SES-001"
    Then the proposal is rejected with reason "INVALID_PROPOSAL_DURATION"
    And proposal "PROP-003" is not registered
