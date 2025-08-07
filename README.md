# Survey Handler Lambda

A Quarkus-based AWS Lambda function that provides survey and agreement management functionality using DynamoDB.

## Features

- **Survey Management**: Create, retrieve, and manage surveys with type/version indexing
- **Agreement Management**: Handle user agreements with version control
- **DynamoDB Integration**: Uses DynamoDB Enhanced Client for data persistence
- **Native Compilation**: Built with GraalVM for fast cold starts
- **API Gateway Integration**: Shares the same API Gateway as the user-handler

## API Endpoints

### Surveys
- `GET /survey` - List all surveys
- `POST /survey` - Create a new survey
- `GET /survey/{typeVersion}` - Get survey by type and version
- `DELETE /survey` - Clear all surveys

### Agreements
- `GET /survey/agreement` - List all agreements
- `POST /survey/agreement` - Create a new agreement
- `GET /survey/agreement/{version}` - Get agreement by version
- `DELETE /survey/agreement` - Clear all agreements

## Data Models

### Survey
```json
{
  "id": "uuid",
  "name": "Survey Name",
  "type": "SCORING",
  "version": "1.0",
  "typeVersion": "SCORING_1.0",
  "sections": {
    "section1": {
      "metadata": {
        "title": "Section Title",
        "weight": 1.0,
        "instruction": "Instructions"
      },
      "data": {
        "field1": {
          "type": "TEXT",
          "required": true,
          "label": "Field Label"
        }
      }
    }
  }
}
```

### Agreement
```json
{
  "id": "uuid",
  "version": "1.0",
  "summary": {
    "en": "Summary in English",
    "es": "Resumen en Español"
  },
  "agreement": {
    "en": "Agreement text in English",
    "es": "Texto del acuerdo en Español"
  },
  "termsConditions": {
    "en": "Terms and conditions in English",
    "es": "Términos y condiciones en Español"
  }
}
```

## Development

### Prerequisites
- Java 17+
- Maven 3.8+
- AWS CLI configured
- Docker (for native builds)

### Local Development
```bash
# Run in development mode
./mvnw quarkus:dev

# Test the endpoints
curl http://localhost:8080/survey
curl http://localhost:8080/survey/agreement
```

### Building
```bash
# JVM build
./mvnw clean package

# Native build
./mvnw clean package -Pnative
```

### Deployment
```bash
# Deploy to dev environment
./deploy.sh dev

# Deploy to staging environment
./deploy.sh staging

# Deploy to production environment
./deploy.sh prod
```

## Infrastructure

The CloudFormation template creates:
- DynamoDB tables for surveys and agreements with GSI indexes
- Lambda function with appropriate IAM permissions
- Integration with existing API Gateway from user-handler

## Configuration

Environment variables:
- `QUARKUS_DYNAMODB_AWS_REGION`: AWS region (default: us-east-1)
- `SURVEYS_TABLE_NAME`: DynamoDB table name for surveys
- `AGREEMENTS_TABLE_NAME`: DynamoDB table name for agreements