package com.enonic.app.guillotine.graphql.fetchers;

import java.util.Map;

import graphql.schema.DataFetchingEnvironment;

import com.enonic.app.guillotine.graphql.ArgumentsValidator;
import com.enonic.app.guillotine.graphql.GuillotineContext;
import com.enonic.app.guillotine.graphql.commands.FindContentsCommand;
import com.enonic.app.guillotine.graphql.helper.GuillotineLocalContextHelper;
import com.enonic.xp.content.ContentService;

public class QueryDataFetcher
    extends QueryBaseDataFetcher
{
    private final ContentService contentService;

    public QueryDataFetcher( final GuillotineContext context, final ContentService contentService )
    {
        super( context );
        this.contentService = contentService;
    }

    @Override
    public Object get( final DataFetchingEnvironment environment )
        throws Exception
    {
        return GuillotineLocalContextHelper.executeInContext( environment, () -> doGet( environment ) );
    }

    private Object doGet( final DataFetchingEnvironment environment )
    {
        ArgumentsValidator.validateArguments( environment.getArguments() );

        Map<String, Object> queryResult = new FindContentsCommand(
            createQueryParams( environment.getArgument( "offset" ), environment.getArgument( "first" ), environment, false ),
            contentService ).execute();

        return queryResult.get( "hits" );
    }
}
