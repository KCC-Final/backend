package com.kcc.groo.bookshelf.dao;

import com.kcc.groo.bookshelf.data.model.BookScrap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IBookScrapRepository {

    /**
     * @param bookScrap
     * @return
     * @author kys
     * @created 2025-10-14
     * 도서 스크랩 생성
     */
    int createBookScrap(BookScrap bookScrap);

    /**
     * @param userId
     * @param ISBN
     * @return
     * @author kys
     * @created 2025-10-14
     * 도서 스크랩 삭제
     */
    int deleteBookScrap(@Param("userId") String userId, @Param("ISBN") String ISBN);

    /**
     * @param userId
     * @param ISBN
     * @return
     * @author kys
     * @created 2025-10-14
     * 도서 스크랩 단건 조회
     */
    BookScrap selectBookScrap(@Param("userId") String userId, @Param("ISBN") String ISBN, @Param("bookshelfId") int bookshelfId);

    /**
     * @param userId
     * @param ISBN
     * @return
     * @author kys
     * @created 2025-10-14
     * 도서 스크랩 목록 조회
     */
    List<BookScrap> BookScrapList(@Param("userId") String userId, @Param("bookshelfId") int bookshelfId);


    /**
     * @param userId
     * @param ISBN
     * @param bookshelfId
     * @return
     * @author kys
     * @created 2025-10-14
     * 한 서재에 같은 도서가 존재하는지 확인
     */
    int checkExistsBookByUserIdAndbookshelfId(@Param("userId") String userId, @Param("ISBN") String ISBN, @Param("bookshelfId") int bookshelfId);

    /**
     * @param userId
     * @param bookshelfId
     * @return
     * @author kys
     * @created 2025-10-14
     * 스크랩된 도서 수 조회
     */
    int countBookScrap(@Param("userId") String userId, @Param("bookshelfId") int bookshelfId);

    /**
     * @param userId
     * @param bookshelfId
     * @param isbnList
     * @return
     * @author kys
     * @created 2025-10-15
     * 스크랩된 도서 선택 삭제
     */
    int deleteBooksByIsbnList(@Param("userId") String userId, @Param("bookshelfId") int bookshelfId, @Param("isbnList") List<String> isbnList);

    /**
     * @param userId
     * @return
     * @author uyh
     * @created 2025-10-16
     * 사용자의 전체 스크랩 수 조회
     */
    int countTotalBookScrapByUserId(@Param("userId") String userId);

    /**
     * 특정 ISBN의 스크랩 수 조회
     *
     * @param ISBN 조회할 ISBN
     * @return int 스크랩 수
     * @author YunSung
     * @created 2025-11-07
     */
    int getBookScrapCountByISBN(@Param("ISBN") String ISBN);


}
