package com.raiks.widgets.infrastructure.jdbi.logging;

import java.sql.SQLException;

import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;

public class SimpleSqlLogger implements SqlLogger {
    @Override
    public void logBeforeExecution(StatementContext context) {
        System.err.println(createLogStatement(context));
    }

    @Override
    public void logAfterExecution(StatementContext context) {
    }

    @Override
    public void logException(StatementContext context, SQLException e) {
    }

    private String createLogStatement(StatementContext context) {
        return String.format("SQL query: %s, params = %s",context.getRenderedSql(), context.getBinding());
    }
}
