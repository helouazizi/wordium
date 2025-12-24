package com.wordium.posts.controllers;



 public class PostsController {

 }





//  public Page<PostResponse> getPosts(Pageable pageable) {
//     Page<Post> postPage = postRepository.findAll(pageable);  // or with Specification for filters

//     // Map entities to DTOs
//     List<PostResponse> dtos = postPage.getContent().stream()
//         .map(postMapper::toResponse)
//         .toList();

//     return new PageImpl<>(dtos, pageable, postPage.getTotalElements());
// }