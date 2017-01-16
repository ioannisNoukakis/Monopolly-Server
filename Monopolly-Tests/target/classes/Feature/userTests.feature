Feature: Tests for the user and auth endpoint.

  Scenario: User should be created with correct payload.
    Given a user payload
    When the user send create request on "/user"
    Then the server should reply with 201

  Scenario: User should not be created with empty payload.
    Given an empty user payload
    When the user send create request on "/user"
    Then the server should reply with 400

  Scenario: User should not be created with a payload with empty fields.
    Given a user payload with empty fields
    When the user send create request on "/user"
    Then the server should reply with 400

  Scenario: A user can auth himself with correct credentials
    Given an auth payload
    When the user send create request on "/auth"
    Then the server should reply with 200
    Then the response body should contains a token