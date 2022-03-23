domain CMD is

  public service register_value(name: in string, value_name: in string, usage: in string, default_value: in string, required: in boolean);

  public service read_command_line();

  public service get_value(name: in string) return string;

end domain;
pragma utility("io.ciera.runtime.util");
