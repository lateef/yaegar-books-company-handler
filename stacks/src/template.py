import sys
from awacs.aws import Policy, Statement, Allow, Principal, Action
from awacs.dynamodb import GetItem, PutItem, Query, UpdateItem
from troposphere import GetAtt, Join, Output, Parameter, Ref, Template
from troposphere.dynamodb import Table, AttributeDefinition, ProvisionedThroughput, KeySchema
from troposphere.awslambda import Function, Code, Environment
from troposphere.iam import Role, Policy as iamPolicy

env = sys.argv[1]

COMPONENT_NAME = env + "YaegarBooksCompany"

t = Template(COMPONENT_NAME)

t.add_version("2010-09-09")

shared_resources_bucket = "sharedbucketsintyaegarboo-s3bucketsharedresources-boencsukew48"
shared_resources_bucket_arn = "arn:aws:s3:::" + shared_resources_bucket

t.add_description(COMPONENT_NAME + " stacks")

companyTable = t.add_resource(
    Table(
        COMPONENT_NAME + "Table",
        AttributeDefinitions=[
            AttributeDefinition(
                AttributeName="uuid",
                AttributeType="S"
            ),
            AttributeDefinition(
                AttributeName="administratorAndName",
                AttributeType="S"
            )
        ],
        KeySchema=[
            KeySchema(
                AttributeName="uuid",
                KeyType="HASH"
            ),
            KeySchema(
                AttributeName="administratorAndName",
                KeyType="RANGE"
            )
        ],
        ProvisionedThroughput=ProvisionedThroughput(
            ReadCapacityUnits=1,
            WriteCapacityUnits=1
        ),
    )
)

companyRole = t.add_resource(
    Role(
        COMPONENT_NAME + "Role",
        AssumeRolePolicyDocument=Policy(
            Version="2012-10-17",
            Statement=[
                Statement(
                    Effect=Allow,
                    Action=[Action("sts", "AssumeRole")],
                    Principal=Principal(
                        "Service", ["lambda.amazonaws.com"]
                    )
                )
            ]
        ),
        Policies=[
            iamPolicy(
                PolicyName=COMPONENT_NAME + "Policy",
                PolicyDocument=Policy(
                    Statement=[
                        Statement(
                            Effect=Allow,
                            Action=[
                                Action("s3", "Get*"),
                                Action("s3", "List*")
                            ],
                            Resource=[
                                shared_resources_bucket_arn,
                            ]
                        ),
                        Statement(
                            Effect=Allow,
                            Action=[Action("logs", "CreateLogGroup"),
                                    Action("logs", "CreateLogStream"),
                                    Action("logs", "PutLogEvents"),
                                    Action("ec2", "CreateNetworkInterface"),
                                    Action("ec2", "DescribeNetworkInterfaces"),
                                    Action("ec2", "DeleteNetworkInterface")],
                            Resource=["*"]),
                        Statement(
                            Effect=Allow,
                            Action=[
                                Action("s3", "Get*"),
                                Action("s3", "List*"),
                                Action("s3", "Put*"),
                                Action("s3", "Delete*")
                            ],
                            Resource=[
                                shared_resources_bucket_arn + "/*"
                            ]
                        ),
                        Statement(
                            Effect=Allow,
                            Action=[GetItem, PutItem, Query, UpdateItem],
                            Resource=[
                                GetAtt(companyTable, "Arn")
                            ]
                        ),
                        Statement(
                            Effect="Allow",
                            Action=[Action("logs", "*")],
                            Resource=["arn:aws:logs:*:*:*"]
                        )
                    ]
                )
            )
        ]
    )
)

companyLambda = t.add_resource(
    Function(
        COMPONENT_NAME + "LambdaFunction",
        Handler="com.yaegar.books.CompanyPersistHandler",
        Role=GetAtt(companyRole, "Arn"),
        Environment=Environment(
            Variables={
                "appEnvironment": env,
                "dynamodbYaegarBooksCompanyTable": Ref(companyTable)
            }
        ),
        Runtime="java8",
        MemorySize=512,
        Timeout=300,
        Code=Code(
            S3Bucket=shared_resources_bucket,
            S3Key="code/lambda/yaegar-books-company-handler-1.0.0-SNAPSHOT.zip"
        )
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
