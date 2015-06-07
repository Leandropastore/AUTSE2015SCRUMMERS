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


INSERT INTO Accounts VALUES
(
    "admin",
    "123",
    "admin@email.com",
    "administrator"
);
INSERT INTO Accounts VALUES
(
    "analyst",
    "123",
    "analyst@email.com",
    "analyst"
);
INSERT INTO Accounts VALUES
(
    "contributor",
    "123",
    "contributor@email.com",
    "contributor"
);
INSERT INTO Accounts VALUES
(
    "moderator",
    "123",
    "moderator@email.com",
    "moderator"
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

INSERT INTO AllArticles (Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor,ModeratedBy,AnalysedBy)
VALUES (
    "Empirical investigation of refactoring effect on software quality",
    "Mohammad Alshayeb",
    "Information and Software Technology",
    "2009",
    "Level 1",
    "released",
    "admin",
    "andy li",
    "andy li"
);

INSERT INTO AllArticles (Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor,ModeratedBy, RejectedReason)
VALUES (
    "A Rejected Article",
    "rejected author",
    "rejected journal",
    "1000",
    "Level 1",
    "rejected",
    "admin",
    "andy li",
    "just testing the rejection"
);

INSERT INTO AllArticles (Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor)
VALUES (
    "this is a FAKE article",
    "FAKE author",
    "FAKE journal",
    "1000",
    "Level 1",
    "new",
    "admin"
);

CREATE TABLE RejectedArticles
(
    ArticleId int NOT NULL UNIQUE,
    Title varchar(255) NOT NULL,
    Moderator varchar(255),
    Reason varchar(8000),
    PRIMARY KEY (ArticleId)
);

INSERT INTO RejectedArticles 
VALUES (
    "2",
    "A Rejected Article",
    "admin",
    "I hate this article"
);

CREATE TABLE CredibilityTable
(
    ArticleID int NOT NULL,
    Rater varchar(255) NOT NULL,
    Rating int,
    Reason varchar(255),
    PRIMARY KEY (ArticleID,Rater)
);

INSERT INTO CredibilityTable VALUES
(
    "1",
    "Andy Li",
    "4",
    "I just want to give him a 4."
);

INSERT INTO CredibilityTable VALUES
(
    "1",
    "Bob",
    "5",
    "I love the number 5."
);


CREATE TABLE MethodologyTable
(
    ArticleID int NOT NULL,
    M_Name varchar(255) NOT NULL,
    Description varchar(8000),
    PRIMARY KEY (ArticleID)
);

INSERT INTO MethodologyTable VALUES
(
    "1",
    "SuperMethod",
    "We cannot describe it"
);


CREATE TABLE PracticeTable
(
    ArticleID int NOT NULL,
    M_Name varchar(255) NOT NULL,
    Description varchar(8000),
    PRIMARY KEY (ArticleID)
);

INSERT INTO PracticeTable VALUES
(
    "1",
    "Refactoring",
    "Refactoring is the process of improving the design of existing code by changing its internal structure without affecting its external behavior"
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

INSERT INTO EvidenceItemTable VALUES
(
    "1",
    "Item A",
    "There is no benifit",
    "someone",
    "something",
    "somewhere",
    "2009-01-01",
    "somehow",
    "somewhy",
    "Some results",
    "Some integrity"

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

INSERT INTO ConfidenceTable VALUES
(
    "1",
    "Item A",
    "Andy Li",
    "5",
    "I just want to give him a 5."
);


CREATE TABLE ResearchDesignTable
(
    ArticleID int NOT NULL,
    qName varchar(255) NOT NULL,
    qDescription varchar(255),
    methodName varchar(255),
    methodDescription varchar(4000),
    metricsName varchar(255),
    metricsDescription varchar(4000),
    participants varchar(255),
    PRIMARY KEY (ArticleID, qName)
);

INSERT INTO ResearchDesignTable VALUES
(
    "1",
    "What is this?",
    "talk about this",
    "XP",
    "what is XP",
    "Metric A",
    "Some sort of metric",
    "Professor"
);

/*
DROP TABLE IF EXISTS MetricTable;
CREATE TABLE MetricTable
(
    ArticleID int NOT NULL,
    ResearchName varchar(255) NOT NULL,
    M_Name varchar(255),
    Description varchar(8000),
    PRIMARY KEY (ArticleID, ResearchName)
);

INSERT INTO MetricTable VALUES
(
    "1",
    "Research A",
    "Metric A",
    "Some sort of metric"
);
*/