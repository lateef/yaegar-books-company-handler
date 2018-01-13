import sys
from troposphere import GetAtt, Join, Output, Parameter, Ref, Template
from troposphere.dynamodb import Table, AttributeDefinition, ProvisionedThroughput, KeySchema

env = sys.argv[1]

COMPONENT_NAME = "YaegarBooksCompanyTable"

t = Template(COMPONENT_NAME)

t.add_version("2010-09-09")

t.add_description(COMPONENT_NAME + " stacks for env " + env)

companyTable = t.add_resource(
    Table(
        "CompanyTable",
        AttributeDefinitions=[
            AttributeDefinition(
                AttributeName="Uuid",
                AttributeType="S"
            ),
            AttributeDefinition(
                AttributeName="Name",
                AttributeType="S"
            )
        ],
        KeySchema=[
            KeySchema(
                AttributeName="Uuid",
                KeyType="HASH"
            ),
            KeySchema(
                AttributeName="Name",
                KeyType="RANGE"
            )
        ],
        ProvisionedThroughput=ProvisionedThroughput(
            ReadCapacityUnits=1,
            WriteCapacityUnits=1
        ),
        TableName=env + "YaegarBooksCompany"
    )
)

t.add_output([
    Output(
        "TableName",
        Value=Ref(companyTable),
        Description="Table name for company",
    )
])

print(t.to_json())
