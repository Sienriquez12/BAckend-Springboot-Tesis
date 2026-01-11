# Role Assignment Feature - Implementation Guide

## Overview
This implementation adds role assignment functionality to the user creation process. Now when creating a user, you must specify a role ID, and the system will validate the role exists and is active before assigning it.

## Changes Made

### 1. Updated DTOs

#### CreateUserRequestDto
- Added `roleId` field with `@NotNull` validation
- This field is required when creating a new user

#### UserAdminDto
- Added `roles` field (List<String>) to display role names in responses

### 2. Updated Services

#### AdminUserServiceImpl
- Now validates that the provided `roleId` exists and is active
- Assigns the role to the user during creation
- Handles role assignment for both new users and reactivated users
- Updated `toDto()` method to include role names in the response

### 3. New Controllers and Services

#### AdminRoleController
- `GET /api/v1/admin/roles` - List all active roles
- `GET /api/v1/admin/roles/{id}` - Get role by ID

#### AdminRoleService & AdminRoleServiceImpl
- Service layer for role management operations

### 4. Updated Repositories

#### UserRoleRepository
- Added `findByIdAndRecordStatusTrue(Long id)` method
- Added `findAllByRecordStatusTrue()` method

## How to Use

### 1. Get Available Roles
First, get the list of available roles:
```
GET /api/v1/admin/roles
```

Response example:
```json
{
  "message": "Roles retrieved",
  "data": [
    {
      "id": 1,
      "name": "ADMIN",
      "description": "Administrator role",
      "hierarchy": 1,
      "isActive": true,
      "recordStatus": true
    },
    {
      "id": 2,
      "name": "STUDENT",
      "description": "Student role", 
      "hierarchy": 5,
      "isActive": true,
      "recordStatus": true
    }
  ]
}
```

### 2. Create User with Role
When creating a user, include the `roleId`:
```
POST /api/v1/admin/users
```

Request body:
```json
{
  "username": "john_doe",
  "email": "john@example.com", 
  "phone": "0987654321",
  "firstName": "John",
  "lastName": "Doe",
  "password": "SecurePassword123!",
  "roleId": 2
}
```

Response:
```json
{
  "message": "User created",
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "phone": "0987654321", 
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["STUDENT"]
  }
}
```

## Validation Rules

1. **roleId is required** - The system will return a 400 error if roleId is not provided
2. **Role must exist** - The system will return a 404 error if the roleId doesn't exist
3. **Role must be active** - The system will return a 404 error if the role exists but is inactive (recordStatus = false)

## Error Handling

- **Role not found**: HTTP 404 - "Role not found or inactive"
- **Missing roleId**: HTTP 400 - "Role ID is required"
- **Email already in use**: HTTP 400 - "Email already in use"
- **Username already in use**: HTTP 400 - "Username already in use"

## User Reactivation

If a user with the same email or username exists but is inactive (recordStatus = false):
- The system will reactivate the user
- Update all fields with the new data provided
- Assign the new role specified in roleId
- Set recordStatus to true

This ensures data consistency and prevents duplicate active users.
