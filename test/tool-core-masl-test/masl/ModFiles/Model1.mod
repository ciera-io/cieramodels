domain TestDomain is

  object Foo;

  public service init(); pragma startup(true);

  object Foo is
    id: preferred integer;

    state init();
    state second();

    event bar();

    transition is
      Non_Existent (bar => Cannot_Happen);
      init (bar => second);
      second (bar => Cannot_Happen);
    end transition;

  end object;

end domain;
