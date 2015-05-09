DROP TABLE IF EXISTS AllArticles;
CREATE TABLE AllArticles
(
    A_Id int NOT NULL UNIQUE AUTO_INCREMENT,
    Title varchar(255) NOT NULL,
    Status varchar(255) DEFAULT "new",
    Location varchar(255),
    PRIMARY KEY (A_Id)
);

-- Create this table for the analysed articles
DROP TABLE IF EXISTS PorcessedDetails;
CREATE TABLE PorcessedDetails
(
    Title varchar(255) NOT NULL,
    AuthorT varchar(255),
    Journal varchar(255),
    YearOfPublish varchar(255),
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