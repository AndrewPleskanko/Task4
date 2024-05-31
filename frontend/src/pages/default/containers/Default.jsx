import { useIntl } from 'react-intl';
import React from 'react';
import Typography from 'components/Typography';

function Default() {
  const { formatMessage } = useIntl();

  return (
      <div>
        <Typography>
          {formatMessage({id: 'title'})}
        </Typography>
        <a href="http://localhost:3050/user-list?lang=ua">User List</a>
      </div>
  );
}

export default Default;
