ALTER TABLE membertable
  MODIFY COLUMN Citizenship VARCHAR(50),
  MODIFY COLUMN Membership_Type_Others VARCHAR(50),
  MODIFY COLUMN Membership_Category_Others VARCHAR(50);

ALTER TABLE heirstable
MODIFY COLUMN Heirs_Relationship VARCHAR(50);

ALTER TABLE currentemprecordtable
  MODIFY COLUMN Country_Of_Assignment VARCHAR(50);