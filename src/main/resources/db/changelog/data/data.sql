-- data.sql

CREATE TABLE IF NOT EXISTS ingredient_type (
    id VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO ingredient_type (id)
VALUES ('WRAP'),
       ('PROTEIN'),
       ('VEGGIES'),
       ('CHEESE'),
       ('SAUCE');

CREATE TABLE ingredient (
    id      VARCHAR(4)  NOT NULL,
    name    VARCHAR(20) NOT NULL,
    type_id VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ingredients_type FOREIGN KEY (type_id) REFERENCES ingredient_type(id)
);

INSERT INTO ingredient (id, name, type_id)
VALUES
    ('FLTO', 'Flour Tortilla', 'WRAP'),
    ('COTO', 'Corn Tortilla', 'WRAP'),
    ('GRBF', 'Ground Beef', 'PROTEIN'),
    ('TMTO', 'Diced Tomatoes', 'VEGGIES'),
    ('LTTC', 'Lettuce', 'VEGGIES'),
    ('CHED', 'Cheddar', 'CHEESE'),
    ('JACK', 'Monterrey Jack', 'CHEESE'),
    ('SLSA', 'Salsa', 'SAUCE'),
    ('SLSR', 'Sour Cream', 'SAUCE');
