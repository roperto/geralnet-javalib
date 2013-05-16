package net.geral.lib.edt;

public class EdtViolationException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private final Object      signature;
  private final Object      source;
  private final boolean     constructor;

  public EdtViolationException(final Object source, final Object signature,
      final boolean constructor) {
    super(signature.toString() + "@" + source.toString() + "["
        + (constructor ? " *constructor*" : "") + "]");
    this.source = source;
    this.signature = signature;
    this.constructor = constructor;
  }

  public EdtViolationException(final String msg) {
    super(msg);
    signature = "Edt required.";
    source = "";
    constructor = false;
  }

  public Object getSignature() {
    return signature;
  }

  public Object getSource() {
    return source;
  }

  public boolean isConstructor() {
    return constructor;
  }
}
