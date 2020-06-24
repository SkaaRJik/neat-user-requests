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
    props: route => ({ step: route.query.step ? Number(route.query.step) : 1 }),
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/projects/NewProject.vue"
      ),
    watch: {
      "$route.query.step"(to, from) {
        console.log("[ProjectsRoutes].$route to, from:", to, from);
      }
    },
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/project/:id",
    name: "project-page",
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/projects/ProjectPage.vue"
      ),
    props: route => ({ projectId: route.params.id }),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/project/:id/configure",
    name: "project-configure",
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/projects/ProjectConfiguration.vue"
      ),
    props: route => ({
      step: route.query.step ? Number(route.query.step) : 1,
      projectId: Number(route.params.id)
    }),
    meta: {
      requiresAuth: true
    }
  }
];

export default projects_routes;
