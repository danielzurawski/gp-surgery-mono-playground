# gp-surgery-mono-playground

## Assumptions
* Monorepo leveraging `lein monolith`, `lein docker`, `docker-compose` for local development of Dockerised `compojure` services
* Event sourcing, Kafka, Kafka Streams, Clojure and Swagger running in Docker
* A React-Native mobile all that allows patients to book appointments, view test results and prescriptions.
* A React web app that allows an imaginary GP Surgery manage patients, appointments, test results and prescriptions.

## Product requirements
1. Login screen 
2. GP Admin 
    1. List of patients
    2. Patient
        1. Bookings
            1. Future bookings
            2. Past bookings
        2. Test results
        3. Prescriptions
            1. View patients prescriptions
            2. Issue new prescriptions
3. Patient
    1. Bookings
        1. Future bookings
        2. Past bookings
    2. Test results
    3. Prescriptions
        1. View my prescriptions


## Usage

lein monolith info

## Installation

### Build images
`lein monolith each ring uberjar`

### Run using docker-compose 
`docker-compose up --build`

## License

Copyright © 2020 Daniel Zurawski

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
