DROP TABLE IF EXISTS Accounts;
DROP TABLE IF EXISTS AllArticles;
DROP TABLE IF EXISTS RejectedArticles;
DROP TABLE IF EXISTS PorcessedDetails;
DROP TABLE IF EXISTS AuthorTable;
DROP TABLE IF EXISTS CredibilityTable;
DROP TABLE IF EXISTS MethodologyTable;
DROP TABLE IF EXISTS PracticeTable;
DROP TABLE IF EXISTS EvidenceItemTable;
DROP TABLE IF EXISTS ConfidenceTable;
DROP TABLE IF EXISTS ResearchDesignTable;
DROP TABLE IF EXISTS MetricTable;


CREATE TABLE Accounts
(
    AccountName varchar(255) NOT NULL UNIQUE,
    Password varchar(255),
    Email varchar(255),
    AccountType varchar(255),
    PRIMARY KEY (AccountName)
);


CREATE TABLE AllArticles
(
    ArticleId int NOT NULL UNIQUE AUTO_INCREMENT,
    Title varchar(255) NOT NULL,
    Authors varchar(255) NOT NULL,
    Journal varchar(255),
    YearOfPublish int,
    ResearchLv varchar(255),
    Status varchar(255) DEFAULT "new",
    RejectedReason varchar(8000) DEFAULT "",
    Contributor varchar(255),
    ModeratedBy varchar(255) DEFAULT "",
    AnalysedBy varchar(255) DEFAULT "",
    PRIMARY KEY (ArticleId)
);

CREATE TABLE RejectedArticles
(
    ArticleId int NOT NULL UNIQUE,
    Title varchar(255) NOT NULL,
    Moderator varchar(255),
    Reason varchar(8000),
    PRIMARY KEY (ArticleId)
);

CREATE TABLE CredibilityTable
(
    ArticleID int NOT NULL,
    Rater varchar(255) NOT NULL,
    Rating int,
    Reason varchar(255),
    PRIMARY KEY (ArticleID,Rater)
);

CREATE TABLE MethodologyTable
(
    ArticleID int NOT NULL,
    M_Name varchar(255) NOT NULL,
    Description varchar(8000),
    PRIMARY KEY (ArticleID)
);


CREATE TABLE PracticeTable
(
    ArticleID int NOT NULL,
    M_Name varchar(255) NOT NULL,
    Description varchar(8000),
    PRIMARY KEY (ArticleID)
);


CREATE TABLE EvidenceItemTable
(
    ArticleID int NOT NULL,
    iName varchar(255) NOT NULL,
    iBenefit varchar(4000) DEFAULT "",
    iWho varchar(255) DEFAULT "",
    iWhat varchar(255) DEFAULT "",
    iWhere varchar(255) DEFAULT "",
    iWhen varchar(255) DEFAULT "",
    iHow varchar(255) DEFAULT "",
    iWhy varchar(255) DEFAULT "",
    iResult varchar(4000) DEFAULT "",
    iIntegrity varchar(4000) DEFAULT "",
    PRIMARY KEY (ArticleID)
);


CREATE TABLE ConfidenceTable
(
    ArticleID int NOT NULL,
    ItemName varchar(255) NOT NULL,
    Rater varchar(255) NOT NULL,
    Rating int,
    Reason varchar(255),
    PRIMARY KEY (ArticleID, ItemName, Rater)
);


CREATE TABLE ResearchDesignTable
(
    ArticleID int NOT NULL,
    qName varchar(255) NOT NULL,
    qDescription varchar(4000),
    methodName varchar(255),
    methodDescription varchar(4000),
    metricsName varchar(255),
    metricsDescription varchar(4000),
    participants varchar(255),
    PRIMARY KEY (ArticleID, qName)
);
