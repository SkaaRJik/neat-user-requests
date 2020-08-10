package ru.filippov.neat.entity.view;

public final class ProjectView {
    public interface Id {}
    public interface Data extends Id {}
    public interface Info extends Id {}

    public interface FullInfo extends Info{ }
}
