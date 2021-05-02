ALTER TABLE experiment_results
    ADD COLUMN prediction_result_file text,
    ADD COLUMN window_train_statistic jsonb;