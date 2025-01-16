CREATE TABLE item
(
    item_id      VARCHAR(255) NOT NULL,
    title        VARCHAR(10) NULL,
    ctype        VARCHAR(255) NULL,
    image_url    TEXT NULL,
    contents_url TEXT NULL,
    position_x   FLOAT NULL,
    position_y   FLOAT NULL,
    height       FLOAT NULL,
    width        FLOAT NULL,
    turnover     FLOAT NULL,
    `order`      INT NULL,
    is_uploaded  TINYINT(1)   NULL,
    page_id      INT          NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY (item_id)
);

ALTER TABLE item
    ADD CONSTRAINT FK_ITEM_ON_PAGE FOREIGN KEY (page_id) REFERENCES page (page_id);