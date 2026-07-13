CREATE TABLE IF NOT EXISTS vocabulary_words (
    id text PRIMARY KEY,
    english text,
    russian text,
    part_of_speech text,
    owner_id text,
    lock text
);

CREATE CUSTOM INDEX IF NOT EXISTS ON vocabulary_words (english)
    USING 'org.apache.cassandra.index.sasi.SASIIndex'
    WITH OPTIONS = {
        'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.StandardAnalyzer',
        'tokenization_enable_stemming': 'true',
        'analyzed': 'true',
        'tokenization_normalize_lowercase': 'true',
        'tokenization_locale': 'en'
        };

CREATE CUSTOM INDEX IF NOT EXISTS ON vocabulary_words (russian)
    USING 'org.apache.cassandra.index.sasi.SASIIndex'
    WITH OPTIONS = {
        'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.StandardAnalyzer',
        'tokenization_enable_stemming': 'true',
        'analyzed': 'true',
        'tokenization_normalize_lowercase': 'true',
        'tokenization_locale': 'ru'
        };