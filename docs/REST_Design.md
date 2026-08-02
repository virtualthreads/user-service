For a Spring Boot microservice, I usually design the API contract **before** writing any code. This avoids redesigning controllers later.

---

# UserController

Base URL

```http
/api/v1/users
```

---

## 1. Register User

```http
POST /api/v1/users
```

### Validations

* First name mandatory
* Email mandatory
* Email format valid
* Email unique
* Password mandatory
* Password policy satisfied
* Gender valid enum
* DOB cannot be future date

### Success

```http
201 Created
```

---

## 2. Get User By Id

```http
GET /api/v1/users/{userId}
```

### Path Validation

* UUID format
* User exists
* User not DELETED

---

## 3. Search Users

```http
POST /api/v1/users/search
```

Supports

* keyword
* status
* gender
* emailVerified
* phoneVerified
* pagination
* sorting

Example

```json
{
    "keyword":"john",
    "status":"ACTIVE",
    "page":0,
    "size":10,
    "sortBy":"firstName",
    "sortDirection":"ASC"
}
```

---

## 4. Update User

```http
PUT /api/v1/users/{userId}
```

### Validations

* UUID valid
* User exists
* Email cannot duplicate another user
* DOB cannot be future
* Status immutable from this API

---

## 5. Update User Status

```http
PATCH /api/v1/users/{userId}/status
```

```json
{
    "status":"LOCKED"
}
```

Allowed

ACTIVE

INACTIVE

LOCKED

SUSPENDED

DELETED

---

## 6. Delete User

Instead of deleting

```http
DELETE /api/v1/users/{userId}
```

Internally

```
status=DELETED
```

Checks

* Exists
* Already deleted?

---

# RoleController

```http
/api/v1/roles
```

---

## Create Role

```http
POST /roles
```

Checks

* Role unique
* Name mandatory

---

## Get All Roles

```http
GET /roles
```

---

## Get Role

```http
GET /roles/{roleId}
```

---

## Update Role

```http
PUT /roles/{roleId}
```

Checks

* Exists
* Duplicate role name

---

## Delete Role

```http
DELETE /roles/{roleId}
```

Checks

* Role assigned to users?
* If yes

```
409 Conflict
```

---

# User Role Controller

```http
/api/v1/users/{userId}/roles
```

---

## Assign Role

```http
POST /users/{userId}/roles
```

Body

```json
{
    "roleId":"uuid"
}
```

Checks

* User exists
* Role exists
* Mapping already exists

---

## Get User Roles

```http
GET /users/{userId}/roles
```

---

## Remove Role

```http
DELETE /users/{userId}/roles/{roleId}
```

Checks

* User exists
* Role exists
* Mapping exists

---

# AddressController

```http
/api/v1/users/{userId}/addresses
```

---

## Create Address

```http
POST /users/{userId}/addresses
```

Checks

* User exists
* Mandatory fields
* Postal code
* Country
* Address Type

If

```
isDefault=true
```

Automatically

```
all other addresses

isDefault=false
```

---

## Get Address

```http
GET /addresses/{addressId}
```

---

## Get User Addresses

```http
GET /users/{userId}/addresses
```

---

## Update Address

```http
PUT /addresses/{addressId}
```

Checks

* Address exists
* User owns address

---

## Set Default Address

```http
PATCH /addresses/{addressId}/default
```

---

## Delete Address

```http
DELETE /addresses/{addressId}
```

Checks

* Exists
* Default address?

If only address

Either

Reject

or

Delete

Business decision.

---

# Request DTOs

## CreateUserRequest

```java
String firstName;

String lastName;

String email;

String phoneNumber;

String password;

Gender gender;

LocalDate dateOfBirth;
```

---

## UpdateUserRequest

```java
String firstName;

String lastName;

String email;

String phoneNumber;

Gender gender;

LocalDate dateOfBirth;
```

---

## UserResponse

```java
UUID userId;

String firstName;

String lastName;

String email;

String phoneNumber;

Gender gender;

LocalDate dateOfBirth;

Boolean emailVerified;

Boolean phoneVerified;

Status status;

LocalDateTime createdAt;

LocalDateTime updatedAt;
```

---

## CreateRoleRequest

```java
String roleName;

String description;
```

---

## RoleResponse

```java
UUID roleId;

String roleName;

String description;
```

---

## AssignRoleRequest

```java
UUID roleId;
```

---

## CreateAddressRequest

```java
AddressType addressType;

String recipientName;

String phoneNumber;

String addressLine1;

String addressLine2;

String landmark;

String city;

String state;

String country;

String postalCode;

Double latitude;

Double longitude;

Boolean isDefault;
```

---

## AddressResponse

```java
UUID addressId;

UUID userId;

AddressType addressType;

String recipientName;

String phoneNumber;

String addressLine1;

String addressLine2;

String landmark;

String city;

String state;

String country;

String postalCode;

Double latitude;

Double longitude;

Boolean isDefault;
```

---

# Common PageRequest DTO

```java
Integer page;

Integer size;

String sortBy;

SortDirection sortDirection;
```

---

# UserSearchRequest

```java
String keyword;

Status status;

Gender gender;

Boolean emailVerified;

Boolean phoneVerified;

Integer page;

Integer size;

String sortBy;

SortDirection sortDirection;
```

---

# Custom Exceptions

Instead of creating dozens of exceptions, I recommend keeping a small, reusable set.

| Exception                        | Used For                                           |
| -------------------------------- | -------------------------------------------------- |
| `ResourceNotFoundException`      | User, Role, Address not found                      |
| `ResourceAlreadyExistsException` | Duplicate email, duplicate role, duplicate mapping |
| `ValidationException`            | Business validation failures                       |
| `InvalidRequestException`        | Invalid UUID, invalid enum, malformed request      |
| `ResourceInUseException`         | Role assigned to users, address cannot be deleted  |
| `UnauthorizedException`          | Authentication failures                            |
| `ForbiddenException`             | Access denied                                      |
| `BusinessException`              | Generic business rule violations                   |
| `DatabaseException`              | Database operation failures                        |
| `InternalServerException`        | Unexpected server errors                           |

---

# Business Conditions to Check

| API                 | Conditions                                                                  |
| ------------------- | --------------------------------------------------------------------------- |
| Create User         | Email unique, valid email, valid password, DOB not in future                |
| Update User         | User exists, email unique, status not changed through this API              |
| Delete User         | User exists, not already deleted (soft delete)                              |
| Get User            | UUID valid, user exists, not deleted                                        |
| Create Role         | Role name unique                                                            |
| Update Role         | Role exists, role name unique                                               |
| Delete Role         | Role exists, not assigned to any users                                      |
| Assign Role         | User exists, role exists, mapping doesn't already exist                     |
| Remove Role         | Mapping exists                                                              |
| Create Address      | User exists, valid address, handle default address logic                    |
| Update Address      | Address exists and belongs to the user                                      |
| Delete Address      | Address exists; decide whether deleting the only/default address is allowed |
| Set Default Address | Address exists, belongs to the user, clear previous default                 |

## Standard API Response

Use a common wrapper across all endpoints:

```java
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
```

For paginated APIs, return:

```java
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
```

This structure keeps all your microservices (User, Product, Order, Inventory, etc.) consistent, making the APIs easier to consume and maintain.
