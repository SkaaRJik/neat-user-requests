const projects_routes = [
  {
    path: "/projects",
    name: "projects",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/projects/Projects.vue"),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/projects/shared",
    name: "shared-projects",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/projects/Projects.vue"),
    props: { shared: true },
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/projects/new",
    name: "new-project",
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/projects/NewProject.vue"
      ),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/projects/:id",
    name: "project-page",
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/projects/ProjectPage.vue"
      ),
    props: route => ({ projectId: route.params.id }),
    meta: {
      requiresAuth: true
    }
  }
];

export default projects_routes;
