package ro.cv.web;

import java.util.List;

import org.springframework.data.domain.Page;

public interface CommentService {

	List<Comment> getAllComments();
	void saveComment(Comment comment);
	Comment getCommentById(long id);
	
	void deleteCommentById(long id);
	Page<Comment> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
