DROP TABLE IF EXISTS AllArticles;
CREATE TABLE AllArticles
(
    A_Id int NOT NULL UNIQUE AUTO_INCREMENT,
    Title varchar(255) NOT NULL,
    Location varchar(255),
    Status varchar(255) DEFAULT "new",
    PRIMARY KEY (A_Id)
);

INSERT INTO AllArticles (Title, Status, Location)
VALUES ("Empirical investigation of refactoring effect on software quality",
"http://www.sciencedirect.com/science/article/pii/S095058490900038X",
"released");

INSERT INTO AllArticles (Title, Status, Location)
VALUES ("I am a FAKE acticle",
"http://www.google.co.nz",
"new");

DROP TABLE IF EXISTS PorcessedDetails;
CREATE TABLE PorcessedDetails
(
    ArticleID int NOT NULL,
    Journal varchar(255),
    YearOfPublish int,
    ResearchLv varchar(255),
    PRIMARY KEY (ArticleID)
);

INSERT INTO PorcessedDetails VALUES
(
    "1",
    "Information and Software Technology",
    "2009",
    "NOT SURE"
);

DROP TABLE IF EXISTS AuthorTable;
CREATE TABLE AuthorTable
(
    ArticleID int NOT NULL,
    FirstName varchar(255),
    LastName varchar(255) NOT NULL,
    PRIMARY KEY (ArticleID, LastName)
);

INSERT INTO AuthorTable VALUES
(
    "1",
    "Mohammad",
    "Alshayeb"
);

DROP TABLE IF EXISTS CredibilityTable;
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


DROP TABLE IF EXISTS MethodologyTable;
CREATE TABLE MethodologyTable
(
    ArticleID int NOT NULL,
    M_Name varchar(255) NOT NULL,
    Description varchar(8000),
    PRIMARY KEY (ArticleID, M_Name)
);

INSERT INTO MethodologyTable VALUES
(
    "1",
    "SuperMethod",
    "We cannot describe it"
);


DROP TABLE IF EXISTS PracticeTable;
CREATE TABLE PracticeTable
(
    ArticleID int NOT NULL,
    M_Name varchar(255) NOT NULL,
    Description varchar(8000),
    PRIMARY KEY (ArticleID, M_Name)
);

INSERT INTO PracticeTable VALUES
(
    "1",
    "Refactoring",
    "Refactoring is the process of improving the design of existing code by changing its internal structure without affecting its external behavior"
);


DROP TABLE IF EXISTS EvidenceItemTable;
CREATE TABLE EvidenceItemTable
(
    ArticleID int NOT NULL,
    ItemName varchar(255) NOT NULL,
    Benifit varchar(4000) DEFAULT "",
    I_Who varchar(255) DEFAULT "",
    I_What varchar(255) DEFAULT "",
    I_Where varchar(255) DEFAULT "",
    I_When date,
    I_result varchar(4000) DEFAULT "",
    I_Integrity varchar(4000) DEFAULT "",
    PRIMARY KEY (ArticleID, ItemName)
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
    "Some results",
    "Some integrity"

);


DROP TABLE IF EXISTS ConfidenceTable;
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


DROP TABLE IF EXISTS ResearchDesignTable;
CREATE TABLE ResearchDesignTable
(
    ArticleID int NOT NULL,
    R_Name varchar(255) NOT NULL,
    Queation varchar(255),
    R_Method varchar(255),
    Nature varchar(255),
    PRIMARY KEY (ArticleID, R_Name)
);

INSERT INTO ResearchDesignTable VALUES
(
    "1",
    "Research A",
    "What is this?",
    "Interview",
    "All people"
);

DROP TABLE IF EXISTS MetricTable;
CREATE TABLE MetricTable
(
    ArticleID int NOT NULL,
    ResearchName varchar(255) NOT NULL,
    M_Name varchar(255),
    Descritions varchar(8000),
    PRIMARY KEY (ArticleID, ResearchName)
);

INSERT INTO MetricTable VALUES
(
    "1",
    "Research A",
    "Metric A",
    "Some sort of metric"
);

-- Create this table for the analysed articles
/*
DROP TABLE IF EXISTS PorcessedDetails;
CREATE TABLE PorcessedDetails
(
    ArticleID int NOT NULL,
    AuthorT varchar(255),
    Journal varchar(255),
    YearOfPublish int,
    CredibilityT varchar(255),
    ResearchLv varchar(255),
    MethodologyT varchar(255),
    PracticeT varchar(255),
    EvidenceItemT varchar(255),
    ResearchDesignT varchar(255),
    PRIMARY KEY (Title)
);

-- create following tables for all analysed articles
    CREATE TABLE AuthorT
(
    FirstName varchar(255),
    LastName varchar(255) NOT NULL,
    PRIMARY KEY (FirstName, LastName)
);

CREATE TABLE CredibilityT
(
    Rater varchar(255) NOT NULL,
    Rating int,
    Reason varchar(255),
    PRIMARY KEY (Rater)
);

CREATE TABLE MethodologyT
(
    M_Name varchar(255) NOT NULL,
    Description varchar(8000),
    PRIMARY KEY (M_Name)
);

CREATE TABLE PracticeT
(
    M_Name varchar(255) NOT NULL,
    Description varchar(8000),
    PRIMARY KEY (M_Name)
);

CREATE TABLE EvidenceItemT
(
    ItemName varchar(255) NOT NULL,
    Benifit varchar(8000) DEFAULT "",
    I_Who varchar(255) DEFAULT "",
    I_Who varchar(255) DEFAULT "",
    I_What varchar(255) DEFAULT "",
    I_Where varchar(255) DEFAULT "",
    I_When date,
    I_result varchar(8000) DEFAULT "",
    I_Integrity varchar(8000) DEFAULT "",
    CondidenceT varchar(255),
    PRIMARY KEY (ItemName)
);
    CREATE TABLE CondidenceT
(
    Rater varchar(255) NOT NULL,
    Rating int,
    Reason varchar(8000);
)


CREATE TABLE ReserchDesignT
(
    R_Name varchar(255) NOT NULL,
    Queation varchar(255),
    R_Method varchar(255),
    MetricT varchar(255),
    Nature varchar(255),
    PRIMARY KEY (R_Name)
);

CREATE TABLE MetricT
(
    M_Name varchar(255),
    Descritions varchar(8000)

);
*/