package com.lt.feedback.dao.jpa;


import com.lt.feedback.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackDao extends JpaRepository<Feedback, Long> {

    @Query(value = "select * from fb_feedback where user_id = :userId and (parent_id is null or parent_id = '') order by create_time desc,id desc \n#pageable\n",
            countQuery = "select count(*) from fb_feedback WHERE user_id = :userId and parent_id is null",
            nativeQuery = true)
    Page<Feedback> findFeedbackList(@Param("userId") String userId, Pageable pageable);

    Feedback getFeedbackByFeedbackId(String feedbackId);

    @Query(value = "select count(*) from fb_feedback where user_id = :userId and reply_status=1", nativeQuery = true)
    Integer countReplyYesFeedback(@Param("userId") String userId);

    @Query(value = "select parent_id as parentId,count(*) as replyCount from fb_feedback where user_id = :userId and (parent_id is null or parent_id = '') and reply_status=1 group by parent_id", nativeQuery = true)
    List<Object> findReplyYesParentFeedBack(@Param("userId") String userId);

    @Query(value = "update fb_feedback set reply_status = 2 where user_id =:userId and (feedback_id=:feedbackId or parent_id=:feedbackId) and reply_status=1", nativeQuery = true)
    @Modifying
    void updateFeedbackSeeReply(@Param("userId") String userId, @Param("feedbackId") String feedbackId);

    @Query(value = "select * from fb_feedback where user_id = :userId and (parent_id = :feedbackId or feedback_id = :feedbackId) order by create_time desc,id desc \n#pageable\n",
            countQuery = "select count(*) from fb_feedback WHERE user_id = :userId and (parent_id = :feedbackId or feedback_id = :feedbackId)",
            nativeQuery = true)
    Page<Feedback> findFeedbackList(@Param("userId") String userId, @Param("feedbackId") String feedbackId, Pageable pageable);
}