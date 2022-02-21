package ro.cv.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepository commentRepository;

	@Override
	public List<Comment> getAllComments() {
		return commentRepository.findAll();
	}

	@Override
	public void saveComment(Comment comment) {
		this.commentRepository.save(comment);
	}

	@Override
	public Comment getCommentById(long id) {
		Optional<Comment> optional = commentRepository.findById(id);
		Comment comment = null;
		if (optional.isPresent()) {
			comment = optional.get();
		} else {
			throw new RuntimeException(" Comment not found for id :: " + id);
		}
		return comment;
	}
	
	@Override
	public void deleteCommentById(long id) {
		this.commentRepository.deleteById(id);
	}

	@Override
	public Page<Comment> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.commentRepository.findAll(pageable);
	}




	
	
}
