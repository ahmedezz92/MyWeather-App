#!/bin/bash

# build.sh - Script to build the project
build_project() {
    echo "Building the project..."
    ./gradlew assembleDebug

    if [ $? -eq 0 ]; then
        echo "Build successful!"
    else
        echo "Build failed!"
        exit 1
    fi
}

# analyze.sh - Script to run static code analysis
run_analysis() {
    echo "Running static code analysis..."

    # Run ktlint for Kotlin code style checks
    ./gradlew ktlintCheck

    # Run detekt for code quality analysis
    ./gradlew detekt

    # Run Android Lint
    ./gradlew lint

    echo "Static analysis complete!"
}

# test.sh - Script to run tests and generate coverage reports
run_tests() {
    echo "Running unit tests and generating coverage reports..."

    # Run tests with coverage
    ./gradlew testDebugUnitTest jacocoTestReport

    if [ $? -eq 0 ]; then
        echo "Tests completed successfully!"
        echo "Coverage report generated at: app/build/reports/jacoco/jacocoTestReport/html/index.html"
    else
        echo "Tests failed!"
        exit 1
    fi
}
chmod +x project_tools.sh

# Main menu
show_menu() {
    echo "Kotlin Android Project Tools"
    echo "1. Build Project"
    echo "2. Run Static Analysis"
    echo "3. Run Tests with Coverage"
    echo "4. Run All"
    echo "5. Exit"
    read -p "Choose an option: " choice

    case $choice in
        1) build_project ;;
        2) run_analysis ;;
        3) run_tests ;;
        4)
            build_project
            run_analysis
            run_tests
            ;;
        5) exit 0 ;;
        *) echo "Invalid option" ;;
    esac
}

# Run menu
show_menu