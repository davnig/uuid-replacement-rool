# UUID Replacement Tool

UUID Replacement Tool is a Java program that scans a given file, looks for UUIDs and replaces each one with a new one
across the entire file.

## Use case

This tool can be useful during a process of import/export with [Keycloak](https://www.keycloak.org/). By default, when
exporting a realm configuration, Keycloak generates a `realm-export.json` file that includes all the primary keys of the
involved entities. Each primary key is a UUID. This is a problem when you want to create multiple realms by importing
the same configuration file (for example when creating different environments), because the second import will always
fail with constraint violations. By using this tool (and by manually replacing the realm name in
the `realm-export.json`) you can avoid this problem.

## How to use it

Just launch the program from the terminal and manually specify the location of the configuration file to use as
argument. If no arguments are specified, the program looks for a `realm-export.json` file in the classpath and uses that
one if found. The output is a new file called `output.json` that is a copy of the original one but with all the UUID
replaced.
