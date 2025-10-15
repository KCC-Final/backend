-- ================================================
-- migration_004_bookshelf_cascade.sql
-- book → bookshelf 외래키에 ON DELETE CASCADE 추가
-- ================================================

USE kcc;

-- 1️⃣ 기존 외래키 제약조건 삭제
ALTER TABLE book
DROP FOREIGN KEY fk_book_bookshelf;

-- 2️⃣ 외래키 재생성 (ON DELETE CASCADE 적용)
ALTER TABLE book
    ADD CONSTRAINT fk_book_bookshelf
        FOREIGN KEY (bookshelf_id)
            REFERENCES bookshelf(bookshelf_id)
            ON DELETE CASCADE;

COMMIT;
