# Backend REST APIs



## Calendars

### End-user endpoints

| Method | API endpoint               | Task                       |
|--------|----------------------------|----------------------------|
| POST   | `/api/v1/calendars`        | Create new calendar        |
| GET    | `/api/v1/calendars`        | Retrieve all calendars     |
| GET    | `/api/v1/calendars/{uuid}` | Retrieve specific calendar |
| PUT    | `/api/v1/calendars`        | Updated specific calendar  |
| DELETE | `/api/v1/calendars`        | Delete calendar(s)         |

### Administrator endpoints

| Method | API endpoint                     | Task                       |
|--------|----------------------------------|----------------------------|
| GET    | `/api/v1/admin/calendars`        | Retrieve all calendars     |
| GET    | `/api/v1/admin/calendars/{uuid}` | Retrieve specific calendar |
| DELETE | `/api/v1/admin/calendars`        | Delete calendar(s)         |



## Events

### End-user endpoints

| Method | API endpoint            | Task                    |
|--------|-------------------------|-------------------------|
| POST   | `/api/v1/events`        | Create new event        |
| GET    | `/api/v1/events`        | Retrieve all events     |
| GET    | `/api/v1/events/{uuid}` | Retrieve specific event |
| PUT    | `/api/v1/events`        | Updated specific event  |
| DELETE | `/api/v1/events`        | Delete event(s)         |

### Administrator endpoints

| Method | API endpoint                  | Task                    |
|--------|-------------------------------|-------------------------|
| GET    | `/api/v1/admin/events`        | Retrieve all events     |
| GET    | `/api/v1/admin/events/{uuid}` | Retrieve specific event |
| DELETE | `/api/v1/admin/events`        | Delete event(s)         |



## Collections

### End-user endpoints

| Method | API endpoint                 | Task                           |
|--------|------------------------------|--------------------------------|
| POST   | `/api/v1/collections`        | Create new collection *)       |                                              
| GET    | `/api/v1/collections`        | Retrieve all collections       |
| GET    | `/api/v1/collections/{uuid}` | Retrieve specific collection   |
| PUT    | `/api/v1/collections`        | Updated specific collection *) |
| DELETE | `/api/v1/collections`        | Delete collection(s)           |

*) Can add/rearrange direct collections/calendars; not nested ones

### Administrator endpoints

| Method | API endpoint                       | Task                         |
|--------|------------------------------------|------------------------------|
| GET    | `/api/v1/admin/collections`        | Retrieve all collections     |
| GET    | `/api/v1/admin/collections/{uuid}` | Retrieve specific collection |
| DELETE | `/api/v1/admin/collections`        | Delete collection(s)         |
