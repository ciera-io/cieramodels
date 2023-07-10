domain HelloWorld is

  public service multiply_by_two(x: in integer); pragma kafka_topic();
  public service add_five(x: in integer); pragma kafka_topic();

  private service testcase1(); pragma scenario(1); pragma test_only();

  terminator Result is
    public service result(r: in string);
  end terminator;

end domain;
