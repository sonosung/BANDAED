new Vue({
  el: ".grid",
  data() {
    return {
      active: false,
      post: "",
      posts: ["Write a message below and hit enter"]
    };
  },
  updated() {
    this.$nextTick(() => {
      let chat = this.$refs.chat;
      chat.scrollTop = chat.scrollHeight;
    });
  },
  methods: {
    createPost() {
      let post = this.post && this.post.trim();
      if (!post) {
        return false;
      }
      this.posts.push(post);
      this.post = "";
    }
  }
});
