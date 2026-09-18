# Design Notes

The project intentionally avoids third-party dependencies. This keeps the evaluator workflow short and makes the application portable across standard JDK installations.

The service layer contains academic rules so the command-line layer stays focused on input/output. Persistence is isolated in `FileDataStore`, while the student collection is isolated in `StudentRepository`. This separation makes the core calculations independently testable.
