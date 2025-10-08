# Contributing to GameAuth

Thank you for your interest in contributing to GameAuth! This document provides guidelines for contributing to the project.

## Getting Started

### Prerequisites
- Java 8 or higher
- Maven 3.6+
- Git
- Basic understanding of authentication concepts

### Development Setup
1. Fork the repository
2. Clone your fork: `git clone <your-fork-url>`
3. Create a feature branch: `git checkout -b feature/your-feature`
4. Make your changes
5. Run tests: `mvn test`
6. Commit your changes: `git commit -m "Add your feature"`
7. Push to your fork: `git push origin feature/your-feature`
8. Create a Pull Request

## Code Style Guidelines

### Java Code Style
- Use 4 spaces for indentation
- Follow Java naming conventions
- Write comprehensive JavaDoc comments
- Use meaningful variable and method names

### Testing Requirements
- Write unit tests for all new functionality
- Maintain at least 80% code coverage
- Use descriptive test method names
- Follow AAA pattern (Arrange, Act, Assert)

### Documentation
- Update README.md for user-facing changes
- Add JavaDoc for all public methods
- Update API documentation as needed
- Include usage examples

## Pull Request Process

### Before Submitting
1. Ensure all tests pass: `mvn test`
2. Check code coverage: `mvn jacoco:report`
3. Run code quality checks
4. Update documentation if needed

### Pull Request Template
```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] No breaking changes (or documented)
```

## Issue Reporting

### Bug Reports
When reporting bugs, please include:
- Clear description of the issue
- Steps to reproduce
- Expected vs actual behavior
- Environment details (Java version, OS, etc.)
- Relevant log output

### Feature Requests
For feature requests, please include:
- Clear description of the feature
- Use case and motivation
- Proposed implementation approach
- Any breaking changes

## Development Workflow

### Branch Naming
- `feature/description` - New features
- `bugfix/description` - Bug fixes
- `docs/description` - Documentation updates
- `refactor/description` - Code refactoring

### Commit Messages
Use clear, descriptive commit messages:
- Start with a verb in imperative mood
- Keep first line under 50 characters
- Add detailed description if needed
- Reference issues when applicable

## Code Review Process

### For Contributors
- Respond to review feedback promptly
- Make requested changes
- Ask questions if feedback is unclear
- Keep PRs focused and manageable

### For Reviewers
- Be constructive and respectful
- Focus on code quality and functionality
- Check for security implications
- Ensure tests are adequate

## Release Process

1. Update version numbers
2. Update CHANGELOG.md
3. Create release notes
4. Tag the release
5. Deploy to repository

## Community Guidelines

- Be respectful and inclusive
- Help others learn and grow
- Follow the code of conduct
- Report inappropriate behavior

## Getting Help

- Check existing issues and discussions
- Ask questions in GitHub discussions
- Contact maintainers for urgent issues
- Join our community chat (if available)

## License

By contributing, you agree that your contributions will be licensed under the same license as the project.
