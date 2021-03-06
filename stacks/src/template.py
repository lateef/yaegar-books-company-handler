import sys
from awacs.aws import Policy, Statement, Allow, Principal, Action
from awacs.dynamodb import GetItem, PutItem, Query, UpdateItem
from troposphere import awslambda, GetAtt, Join, Output, Parameter, Ref, Template
from troposphere.dynamodb import Table, AttributeDefinition, ProvisionedThroughput, KeySchema
from troposphere.awslambda import Function, Code, Environment
from troposphere.iam import Role, Policy as iamPolicy

env = sys.argv[1]

COMPONENT_NAME = env + "YaegarBooksCompany"

t = Template(COMPONENT_NAME)

t.add_version("2010-09-09")

shared_resources_bucket = t.add_parameter(Parameter("SharedResourcesBucket", Type="String"))
shared_resources_bucket_arn = t.add_parameter(Parameter("SharedResourcesBucketArn", Type="String"))
build_version = t.add_parameter(Parameter("BuildVersion", Type="String"))

t.add_description(COMPONENT_NAME + " stacks")

companyTable = t.add_resource(
    Table(
        "Table",
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
        )
    )
)

companyTableAN = t.add_resource(
    Table(
        "TableAN",
        AttributeDefinitions=[
            AttributeDefinition(
                AttributeName="administratorAndName",
                AttributeType="S"
            ),
            AttributeDefinition(
                AttributeName="uuid",
                AttributeType="S"
            )
        ],
        KeySchema=[
            KeySchema(
                AttributeName="administratorAndName",
                KeyType="HASH"
            ),
            KeySchema(
                AttributeName="uuid",
                KeyType="RANGE"
            )
        ],
        ProvisionedThroughput=ProvisionedThroughput(
            ReadCapacityUnits=1,
            WriteCapacityUnits=1
        ),
    )
)

companyIndustryTable = t.add_resource(
    Table(
        "IndustryTable",
        AttributeDefinitions=[
            AttributeDefinition(
                AttributeName="uuid",
                AttributeType="S"
            ),
            AttributeDefinition(
                AttributeName="industry",
                AttributeType="S"
            )
        ],
        KeySchema=[
            KeySchema(
                AttributeName="uuid",
                KeyType="HASH"
            ),
            KeySchema(
                AttributeName="industry",
                KeyType="RANGE"
            )
        ],
        ProvisionedThroughput=ProvisionedThroughput(
            ReadCapacityUnits=1,
            WriteCapacityUnits=1
        )
    )
)

companyRole = t.add_resource(
    Role(
        "Role",
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
                PolicyName="Policy",
                PolicyDocument=Policy(
                    Statement=[
                        Statement(
                            Effect=Allow,
                            Action=[
                                Action("s3", "Get*"),
                                Action("s3", "List*")
                            ],
                            Resource=[
                                Ref(shared_resources_bucket_arn),
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
                                Join("", [Ref(shared_resources_bucket_arn), "/*"])
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
                            Effect=Allow,
                            Action=[GetItem, PutItem, Query, UpdateItem],
                            Resource=[
                                GetAtt(companyTableAN, "Arn")
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

lambdaCode = awslambda.Code(
    S3Bucket=Ref(shared_resources_bucket),
    S3Key=Join("", ["code/lambda/yaegar-books-company-handler", "-",
                    Ref(build_version), ".zip"])
)

companyLambda = t.add_resource(
    Function(
        "LambdaFunction",
        Handler="com.yaegar.books.CompanyHandler",
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
        Code=lambdaCode
    )
)

t.add_output([
    Output(
        "TableName",
        Value=Ref(companyTable),
        Description="Table name for company",
    )
])

t.add_output([
    Output(
        "TableNameAN",
        Value=Ref(companyTableAN),
        Description="Table name for company",
    )
])

t.add_output([
    Output(
        "TableNameIndustry",
        Value=Ref(companyIndustryTable),
        Description="Table name for company industry",
    )
])

print(t.to_json())
