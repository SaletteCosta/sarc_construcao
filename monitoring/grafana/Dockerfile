FROM grafana/grafana:latest

# Copy provisioning files
COPY provisioning /etc/grafana/provisioning

# Copy dashboards
COPY dashboards /var/lib/grafana/dashboards

USER grafana
